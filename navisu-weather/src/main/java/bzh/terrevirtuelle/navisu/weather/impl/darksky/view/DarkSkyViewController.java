/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bzh.terrevirtuelle.navisu.weather.impl.darksky.view;

import bzh.terrevirtuelle.navisu.app.guiagent.svg.SVGContent;
import bzh.terrevirtuelle.navisu.app.guiagent.svg.SVGLoader;
import bzh.terrevirtuelle.navisu.widgets.impl.Widget2DController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import static bzh.terrevirtuelle.navisu.app.guiagent.utilities.Translator.tr;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.FIOAlerts;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.FIOCurrently;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.FIODaily;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.FIOFlags;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.FIOHourly;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.FIOMinutely;
import bzh.terrevirtuelle.navisu.weather.impl.darksky.controller.ForecastIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @date 6 mars 2015
 * @author Serge Morvan
 */
public class DarkSkyViewController
        extends Widget2DController
        implements Initializable {

    @FXML
    public Group weatherViewPanel;
    @FXML
    public ImageView quit;
    @FXML
    Label title;
    @FXML
    Label windSpeedLabel;
    @FXML
    Label windSpeedData;
    @FXML
    Label windBearingLabel;
    @FXML
    Label windBearingData;
    @FXML
    Label humidityLabel;
    @FXML
    Label humidityData;
    @FXML
    Label visibilityLabel;
    @FXML
    Label visibilityData;
    @FXML
    Label temperatureLabel;
    @FXML
    Label temperatureData;
    @FXML
    Label pressureLabel;
    @FXML
    Label pressureData;
    @FXML
    Label dewPointLabel;
    @FXML
    Label dewPointData;
    @FXML
    Label apparentTemperatureLabel;
    @FXML
    Label apparentTemperatureData;
    @FXML
    Label precipProbabilityLabel;
    @FXML
    Label precipProbabilityData;
    @FXML
    Label precipTypeLabel;
    @FXML
    Label precipTypeData;
    @FXML
    Label precipIntensityLabel;
    @FXML
    Label precipIntensityData;
    @FXML
    Label cloudCoverLabel;
    @FXML
    Label cloudCoverData;
    @FXML
    Label summaryLabel;
    @FXML
    Label timeLabel;
    @FXML
    Label summaryData;
    @FXML
    Label timeData;
    @FXML
    LineChart hoursLineChart;
    @FXML
    LineChart daysLineChart;
    @FXML
    StackPane iconId;
    String FXML = "weatherViewPanel.fxml";
    private ForecastIO fio;
    private Map<String, List<Double>> hoursDataMap;

    public DarkSkyViewController() {
        setMouseTransparent(false);
        hoursDataMap = new HashMap<>();
        hoursDataMap.put("WindSpeed", new ArrayList<>());
        hoursDataMap.put("WindBearing", new ArrayList<>());
        load();
    }

    private void load() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void setTitle(Label title) {
        this.title = title;
    }

    public Label getTitle() {
        return title;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        windSpeedLabel.setText(tr("wheater.darsky.windSpeed"));
        windBearingLabel.setText(tr("wheater.darsky.windBearing"));
        visibilityLabel.setText(tr("wheater.darsky.visibility"));
        humidityLabel.setText(tr("wheater.darsky.humidity"));
        temperatureLabel.setText(tr("wheater.darsky.temperature"));
        apparentTemperatureLabel.setText(tr("wheater.darsky.apparentTemperature"));
        pressureLabel.setText(tr("wheater.darsky.pressure"));
        dewPointLabel.setText(tr("wheater.darsky.dewPoint"));
        precipProbabilityLabel.setText(tr("wheater.darsky.precipProbability"));
        precipTypeLabel.setText(tr("wheater.darsky.precipType"));
        precipIntensityLabel.setText(tr("wheater.darsky.precipIntensity"));
        cloudCoverLabel.setText(tr("wheater.darsky.cloudCover"));
        summaryLabel.setText(tr("wheater.darsky.summary"));
        timeLabel.setText(tr("wheater.darsky.time"));
        quit.setOnMouseClicked((MouseEvent event) -> {
            setVisible(false);
        });
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Hours");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Data");
        
        iconId.setBackground(new Background(new BackgroundFill(
                Color.AQUAMARINE,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        SVGContent content = SVGLoader.load(getClass().getResource("meteoicones/Cloud-Download.svg").toString());
        iconId.getChildren().add(content);
    }

    public void showData(ForecastIO fio) {
        this.fio = fio;
        FIOCurrently currently = new FIOCurrently(fio);
        this.windSpeedData.setText(Double.toString(currently.get().windSpeed()));
        this.windBearingData.setText(Double.toString(currently.get().windBearing()));
        this.visibilityData.setText(Double.toString(currently.get().visibility()));
        this.humidityData.setText(Double.toString(currently.get().humidity()));
        this.temperatureData.setText(Double.toString(currently.get().temperature()));
        this.apparentTemperatureData.setText(Double.toString(currently.get().apparentTemperature()));
        this.pressureData.setText(Double.toString(currently.get().pressure()));
        this.dewPointData.setText(Double.toString(currently.get().dewPoint()));
        this.precipProbabilityData.setText(Double.toString(currently.get().precipProbability()));
        this.precipTypeData.setText(currently.get().precipType());
        this.precipIntensityData.setText(Double.toString(currently.get().precipIntensity()));
        this.cloudCoverData.setText(Double.toString(currently.get().cloudCover()));
        this.summaryData.setText(currently.get().summary());
        this.timeData.setText(currently.get().time());
        // print(fio);
        showHoursData(fio);
    }

    public void showHoursData(ForecastIO fio) {
        FIOHourly hourly = new FIOHourly(fio);
        XYChart.Series<Integer, Double> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("WindSpeed");
        if (hourly.hours() > 0) {
            for (int i = 0; i < hourly.hours(); i++) {
                XYChart.Data<Integer, Double> data 
                        = new XYChart.Data<>(i, hourly.getHour(i).windSpeed());
                dataSeries1.getData().add(data);
                data.nodeProperty().addListener(observable -> {
                    final Node node = data.getNode();
                    final Tooltip tooltip = new Tooltip(data.getYValue().toString()+" Kn" + '\n' 
                            +data.getXValue().toString() + " h");
                    Tooltip.install(node, tooltip);
                });
            }
        }
      //  hoursLineChart.getXAxis().setTi
        hoursLineChart.getData().addAll(dataSeries1);
    }

    public void print(ForecastIO fio) {
        //Response Headers info
        System.out.println("Response Headers:");
        System.out.println("Cache-Control: " + fio.getHeaderCache_Control());
        System.out.println("Expires: " + fio.getHeaderExpires());
        System.out.println("X-Forecast-API-Calls: " + fio.getHeaderX_Forecast_API_Calls());
        System.out.println("X-Response-Time: " + fio.getHeaderX_Response_Time());
        System.out.println("\n");

        //ForecastIO info
        System.out.println("Latitude: " + fio.getLatitude());
        System.out.println("Longitude: " + fio.getLongitude());
        System.out.println("Timezone: " + fio.getTimezone());
        System.out.println("Offset: " + fio.offsetValue());
        System.out.println("\n");

        //Currently data
        FIOCurrently currently = new FIOCurrently(fio);

        System.out.println("\nCurrently\n");
        String[] f = currently.get().getFieldsArray();
        for (int i = 0; i < f.length; i++) {
            System.out.println(f[i] + ": " + currently.get().getByKey(f[i]));
        }
        System.out.println("\n");

        //Minutely data
        FIOMinutely minutely = new FIOMinutely(fio);
        if (minutely.minutes() < 0) {
            System.out.println("No minutely data.");
        } else {
            System.out.println("\nMinutely\n");
        }
        for (int i = 0; i < minutely.minutes(); i++) {
            String[] m = minutely.getMinute(i).getFieldsArray();
            System.out.println("Minute #" + (i + 1));
            for (int j = 0; j < m.length; j++) {
                System.out.println(m[j] + ": " + minutely.getMinute(i).getByKey(m[j]));
            }
            System.out.println("\n");
        }
        //Hourly data
        FIOHourly hourly = new FIOHourly(fio);
        if (hourly.hours() < 0) {
            System.out.println("No hourly data.");
        } else {
            System.out.println("\nHourly:\n");
        }
        for (int i = 0; i < hourly.hours(); i++) {
            String[] h = hourly.getHour(i).getFieldsArray();
            System.out.println("Hour #" + (i + 1));
            for (int j = 0; j < h.length; j++) {
                System.out.println(h[j] + ": " + hourly.getHour(i).getByKey(h[j]));
            }
            System.out.println("\n");
        }
        //Daily data
        FIODaily daily = new FIODaily(fio);
        if (daily.days() < 0) {
            System.out.println("No daily data.");
        } else {
            System.out.println("\nDaily:\n");
        }
        for (int i = 0; i < daily.days(); i++) {
            String[] h = daily.getDay(i).getFieldsArray();
            System.out.println("Day #" + (i + 1));
            for (int j = 0; j < h.length; j++) {
                System.out.println(h[j] + ": " + daily.getDay(i).getByKey(h[j]));
            }
            System.out.println("\n");
        }
        //Flags data
        FIOFlags flags = new FIOFlags(fio);
        System.out.println("Available Flags: ");
        for (int i = 0; i < flags.availableFlags().length; i++) {
            System.out.println(flags.availableFlags()[i]);
        }
        System.out.println("\n");
        for (int i = 0; i < flags.metarStations().length; i++) {
            System.out.println("Metar Station: " + flags.metarStations()[i]);
        }
        System.out.println("\n");
        for (int i = 0; i < flags.isdStations().length; i++) {
            System.out.println("ISD Station: " + flags.isdStations()[i]);
        }
        System.out.println("\n");
        for (int i = 0; i < flags.sources().length; i++) {
            System.out.println("Source: " + flags.sources()[i]);
        }
        System.out.println("\n");
        System.out.println("Units: " + flags.units());
        System.out.println("\n");

        //Alerts data
        FIOAlerts alerts = new FIOAlerts(fio);
        if (alerts.NumberOfAlerts() <= 0) {
            System.out.println("No alerts for this location.");
        } else {
            System.out.println("Alerts");
            for (int i = 0; i < alerts.NumberOfAlerts(); i++) {
                System.out.println(alerts.getAlert(i));
            }
        }
    }

}
