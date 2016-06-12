/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.sailingdirections.impl.controller;

import bzh.terrevirtuelle.navisu.api.progress.ProgressHandle;
import bzh.terrevirtuelle.navisu.app.guiagent.GuiAgentServices;
import bzh.terrevirtuelle.navisu.database.graph.neo4J.GraphDatabaseComponentServices;
import bzh.terrevirtuelle.navisu.widgets.impl.Widget2DController;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

/**
 *
 * @author serge
 * @date Jun 6, 2016
 *
 */
public class SailingDirectionsTreeViewController
        extends Widget2DController
        implements Initializable {

    private static SailingDirectionsTreeViewController INSTANCE;
    private final GuiAgentServices guiAgentServices;
    private final String FXML = "sailingDirectionsTreeView.fxml";
    private final String USER_HOME = System.getProperty("user.home");
    private final String DB_HOME = USER_HOME + "/" + ".navisu/databases/";
    private final String GRAPH_KB_IN = DB_HOME + "InKB";
    private final String ROOT = "data/sd/shom/";
    private final String IN_SHOM_KB_FILE_NAME = ROOT + "KB-v2.4.cypher";
    private final String SITE = "Baie de Banyuls";
    private final String VOLUME = "D21";

    //Requests
    private final String REQUEST_0 = "MATCH (n) WHERE n.ID =~ 'D21.*' RETURN n";
    private final String REQUEST_1 = " MATCH (n:`RACINE`)-[r*]->(b:`SECTION`) WHERE n.ID='D21' "
            + "RETURN n.TITRE AS racineTitre, n.ID as racineId,r,b.TITRE AS sectionTitre, b.ID AS sectionId";
    private final String REQUEST_2 = "MATCH (n)  RETURN n";
    /*
    Récupérer tous les polygones
     */
    private final String REQUEST_3 = "MATCH (n:`POLYGONE`) RETURN n";
    /* 
    Pour chaque polygone, récupérer le texte + la structure
    Il suffit de remplacer [Baie de Banyuls] par le nom du polygone désiré.
    
    On récupère d'abord le polygone ((polygone:`POLYGONE`)) qui nous intéresse (et on le cherche par nom via WHERE).
    On récupère la section à laquelle il appartient (section:`SECTION`).
    On récupère les instances géolocalisées présentes dans le polygone (<-[:`isIn`]-(instance_geo:`INSTANCE_GEO`)).
    On récupère les relations de cette ou ces instances géolocalisées (<-[r]-(relation:`RELATION`)).
    On récupère les instances liées à ces relations ((relation:`RELATION`)-->(instanceLiee)).
    
    On obtient donc : le polygone + les instances géolocalisées dans le polygone 
    + les relations de ces dernières (qui sont exprimées sous forme de nœuds) 
    + le numéro de l'instance dans la relation via type(r)
    + les autres instances géolocalisées ou non des relations
    + la section à laquelle appartient le polygone.
     */
    private final String REQUEST_4 = "MATCH (section:`SECTION`)-[:`isInside`]->(polygone:`POLYGONE`)<-[:`isIn`]-(instance_geo:`INSTANCE_GEO`)<-[r]-(relation:`RELATION`)-->(instanceLiee)\n"
            + "    WHERE polygone.NOM_ZONE=\"[" + SITE + "]\"\n"
            + "    RETURN polygone,instance_geo,relation,type(r) AS numeroInstance,instanceLiee,section";
    /* 
    Récupérer la structure du volume
    La requête n'est pas très élégante mais le niveau de profondeur de la structure 
    était fixe on peut se permettre de faire ce genre de requête.
     */
    private final String REQUEST_5 = "MATCH (n:`RACINE`)-->(a:`SECTION`)-->(b:`SECTION`)-->(c:`SECTION`)\n"
            + "    WHERE n.ID=\"" + VOLUME + "\"\n"
            + "    RETURN n,a,b,c";

    private GraphDatabaseService graphDb;
    private final GraphDatabaseComponentServices graphDatabaseComponentServices;

    @FXML
    public Pane view;
    @FXML
    public Button quit;
    @FXML
    public Slider opacitySlider;
    @FXML
    public GridPane bookGP;

    TreeView<String> treeView;
    TreeItem<String> rootNode;
    private String ROOT_ICON = "folder_16.png";
    private String CHAPTER_ICON = "folder_16.png";

    String book = null;
    String chapter = "";
    String subChapter = "";
    String para = "";
    String[] tab;
    String[] nodeTab;
    String[] valueTab;
    String[] nodeValue;
    String[] titre = null;
    String[] niveau = null;
    String[] id;
    String bookId;
    String chapterId = "X";
    int chapterSize = 0;
    String subChapterId = "X";
    int subChapterSize = 0;
    String paraId = "X";
    int paraSize = 0;

    public static SailingDirectionsTreeViewController getInstance(
            KeyCode keyCode, KeyCombination.Modifier keyCombination,
            GuiAgentServices guiAgentServices,
            GraphDatabaseComponentServices graphDatabaseComponentServices) {
        if (INSTANCE == null) {
            INSTANCE = new SailingDirectionsTreeViewController(keyCode, keyCombination,
                    guiAgentServices, graphDatabaseComponentServices);
        }
        guiAgentServices.getScene().addEventFilter(KeyEvent.KEY_RELEASED, INSTANCE);
        guiAgentServices.getRoot().getChildren().add(INSTANCE);
        INSTANCE.setVisible(true);
        return INSTANCE;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public SailingDirectionsTreeViewController(
            KeyCode keyCode, KeyCombination.Modifier keyCombination,
            GuiAgentServices guiAgentServices,
            GraphDatabaseComponentServices graphDatabaseComponentServices) {
        super(keyCode, keyCombination);
        this.guiAgentServices = guiAgentServices;
        this.graphDatabaseComponentServices = graphDatabaseComponentServices;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quit.setOnMouseClicked((MouseEvent event) -> {
            guiAgentServices.getRoot().getChildren().remove(INSTANCE);
            INSTANCE.setVisible(false);
        });
        opacitySlider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            Platform.runLater(() -> {
                view.setOpacity(opacitySlider.getValue());
            });
        });
        guiAgentServices.getJobsManager().newJob("retrieveAll", (ProgressHandle progressHandle) -> {
            graphDb = graphDatabaseComponentServices.newEmbeddedDatabase(GRAPH_KB_IN);
            Result result = graphDb.execute(REQUEST_5);
            createVolumeTree(result);
        });
    }

    private void createVolumeTree(Result result) {
        String res;

        res = result.resultAsString();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tab = res.split("\n");

                Stack<String> stack = new Stack();
                stack.addAll(Arrays.asList(tab));
                String s;
                while (!stack.empty()) {
                    s = stack.pop();
                    if (s.contains("Node")) {
                        // System.out.println(s);
                        nodeTab = s.split("\\|");
                        for (String nt : nodeTab) {
                            valueTab = nt.split("\\{");
                            if (valueTab.length > 1) {
                                valueTab[1] = valueTab[1].replace("}", "").trim();
                                nodeValue = valueTab[1].split(",");
                                for (String nv : nodeValue) {
                                    if (nv.contains("TITRE")) {
                                        titre = nv.split("\"");
                                    } else if (nv.contains("NIVEAU")) {
                                        niveau = nv.split("\"");

                                    } else if (nv.contains("ID")) {
                                        id = nv.split("\"");
                                    }
                                }
                                if (niveau != null && titre != null && id != null) {
                                    if (book == null) {
                                        if (niveau[1].contains("0")) {
                                            book = titre[1];
                                            bookId = id[1];
                                            rootNode = new TreeItem<>(book, new ImageView(new Image(getClass().getResourceAsStream(ROOT_ICON))));
                                            treeView = new TreeView<>(rootNode);
                                            treeView.setEditable(true);
                                            treeView.setCellFactory((TreeView<String> p) -> new TextFieldTreeCellImpl());
                                            bookGP.getChildren().add(treeView);
                                        }
                                    } else if (niveau[1].contains("1")) {
                                        if (!id[1].equals(chapterId)) {
                                            chapterId = id[1];
                                            chapter = titre[1];
                                            TreeItem chapterItem
                                                    = new TreeItem<>(chapter, new ImageView(new Image(getClass().getResourceAsStream(CHAPTER_ICON))));
                                            rootNode.getChildren().add(chapterItem);
                                            chapterSize++;
                                            subChapterSize = 0;
                                            paraSize = 0;
                                        }
                                    } else if (niveau[1].contains("2")) {
                                        if (!id[1].contains(subChapterId)) {
                                            paraSize = 0;
                                            subChapterId = id[1];
                                            subChapter = titre[1];
                                            TreeItem subChapterItem
                                                    = new TreeItem<>(subChapter, new ImageView(new Image(getClass().getResourceAsStream(CHAPTER_ICON))));
                                            rootNode.getChildren().get(chapterSize - 1)
                                                    .getChildren().add(subChapterItem);
                                            subChapterSize++;
                                        }
                                    } else if (niveau[1].contains("3")) {
                                        if (!id[1].contains(paraId)) {
                                            paraId = id[1];
                                            para = titre[1];
                                            TreeItem paraItem
                                                    = new TreeItem<>(para, new ImageView(new Image(getClass().getResourceAsStream(CHAPTER_ICON))));
                                            rootNode.getChildren().get(chapterSize - 1)
                                                    .getChildren().get(subChapterSize - 1)
                                                    .getChildren().add(paraItem);
                                            paraSize++;
                                        }
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }
        );
    }

    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private final ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Add item");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(new EventHandler() {
                @Override
                public void handle(Event t) {
                    TreeItem item = new TreeItem<>("New item");
                    getTreeItem().getChildren().add(item);
                }
            });
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(getTreeItem().getGraphic());
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(getTreeItem().getGraphic());
                if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null) {
                    setContextMenu(addMenu);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased((KeyEvent t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });

        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }

}
