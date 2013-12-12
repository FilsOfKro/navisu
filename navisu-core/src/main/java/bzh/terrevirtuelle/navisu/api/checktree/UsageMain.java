package bzh.terrevirtuelle.navisu.api.checktree;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * NaVisu
 *
 * @author tibus
 * @date 10/12/2013 21:23
 */
public class UsageMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, Color.TRANSPARENT);
        stage.setScene(scene);

        CheckTree tree = new CheckTree();

        TreeItem<CheckTreeItem.CheckTreeItemImpl> rootItem = new TreeItem<>();
        tree.setRoot(rootItem);
        tree.setShowRoot(false);

        Action[] actionList = new Action[] {
            Action.create("Action 1", (source) -> System.out.println("Do Action 1 source@" + source)),
            Action.create("Action 2", (source) -> System.out.println("Do Action 2 source@" + source)),
            Action.create("Action 3", (source) -> System.out.println("Do Action 3 source@" + source))
        };
        
        CheckTreeItem.CheckTreeItemImpl item1 = new CheckTreeItem.CheckTreeItemImpl("Item 1", true, actionList);
        CheckTreeItem.CheckTreeItemImpl item2 = new CheckTreeItem.CheckTreeItemImpl("Item 2", false, actionList);

        rootItem.getChildren().addAll(new TreeItem<>(item1), new TreeItem<>(item2));

        root.setCenter(tree);
        stage.setTitle("Usage of check tree");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(UsageMain.class);
    }
}
