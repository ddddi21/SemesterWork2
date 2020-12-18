package drawing;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Connection;
import services.SendDrawingService;

import java.lang.reflect.Field;

public class Draw  {
    public ColorPicker colorPicker;
    public javafx.scene.control.TextField txtName;
    public javafx.scene.control.TextField txtInput;
    javafx.scene.control.ScrollPane scrollPane;
    public javafx.scene.control.TextField roomField;
    public static TextArea txtAreaDisplay;
    public Connection connection;
    SendDrawingService drawingService = new SendDrawingService();
    StringBuilder getDrawing = new StringBuilder();
    String[] drawsAtributes = new String[3];
    public Boolean isStart = false;
    public Integer id;

    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        VBox vBox = new VBox();
        scrollPane = new javafx.scene.control.ScrollPane();
        HBox hBox = new HBox();

        getDrawing = new StringBuilder();

        txtAreaDisplay = new TextArea();
        txtAreaDisplay.setEditable(false);
        scrollPane.setContent(txtAreaDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        final Canvas canvas = new Canvas(600, 300);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        //если игрок ведущий, то отрисовываем соответсующий юай и добавляем соотв функционал
        if(connection.isCommander) {
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(event.getX(), event.getY());
                            graphicsContext.setStroke(colorPicker.getValue());
                            graphicsContext.stroke();
                            isStart = true;
                            drawingService.isStartGame(isStart);
                            //если он начал рисовать передаю тру
                        }
                    });

            canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            isStart = true;
                            drawingService.isStartGame(isStart);
                            double x = event.getX();
                            double y= event.getY();
                            graphicsContext.lineTo(x,y);
                            graphicsContext.setStroke(colorPicker.getValue());
                            graphicsContext.stroke();
                            Color color = colorPicker.getValue();
                            drawingService.receivePictureFromCommander(x,y,colorPicker.getValue());
                        }
                    });

            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {

                        }
                    });

            Button clean = new Button();

            clean.setText("clean all");
            clean.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    double canvasWidth = graphicsContext.getCanvas().getWidth();
                    double canvasHeight = graphicsContext.getCanvas().getHeight();

                    graphicsContext .clearRect(0,0,canvasWidth,canvasHeight);
                    graphicsContext.setStroke(Color.BLACK);
                    graphicsContext.setLineWidth(5);
                    graphicsContext.fill();
                    graphicsContext.strokeRect(
                            0,              //x of the upper left corner
                            0,              //y of the upper left corner
                            canvasWidth,    //width of the rectangle
                            canvasHeight);  //height of the rectangle
                    graphicsContext.setStroke(colorPicker.getValue());
                    graphicsContext.setLineWidth(3);
                }
            });

            HBox topBox = new HBox();

            topBox.getChildren().addAll(colorPicker,clean);
            HBox.setHgrow(clean,Priority.ALWAYS);
            HBox.setHgrow(colorPicker,Priority.ALWAYS);
            vBox.getChildren().addAll(topBox,canvas, scrollPane);

        } else{
            txtName = new javafx.scene.control.TextField();
            txtName.setPromptText("Name");
            txtName.setTooltip(new Tooltip("Write your name. "));
            txtInput = new javafx.scene.control.TextField();
            txtInput.setPromptText("New message");
            txtInput.setTooltip(new Tooltip("Write your message. "));
            javafx.scene.control.Button btnSend = new Button("Send");
            btnSend.setOnAction(new ButtonListener());
            hBox.getChildren().addAll(txtName, txtInput, btnSend);
            HBox.setHgrow(txtInput, Priority.ALWAYS);
            vBox.getChildren().addAll(canvas, scrollPane, hBox);

            //если начал рисовать вывзыается метод для отрисовки
            if(drawingService.sendIsStartGame())
            draw(graphicsContext);

        }

        vBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, 800, 625);
        primaryStage.setTitle("крокодильчик");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                connection.disconnect();
            }
        });
    }


    private void initDraw(GraphicsContext gc) {

        colorPicker = new ColorPicker();

        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setFill(colorPicker.getValue());
        gc.setStroke(colorPicker.getValue());
        gc.setLineWidth(3);
    }


    public synchronized void getPicture(double x, double y, GraphicsContext graphicsContext) {
        graphicsContext.beginPath();
        graphicsContext.moveTo(x,y);
        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.stroke();
    }

    //слушатель на отправку сообщений в чат, который впоследствии отправляет другим клиентам сообщение
    public class ButtonListener implements EventHandler<javafx.event.ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent e) {
            String username = txtName.getText().trim();
            String message = txtInput.getText().trim();

            if (username.length() == 0) {
                username = "Unknown";
            }
            if (message.length() == 0) {
                return;
            }

            connection.sendString("[" + username + "]: " + message + "");

            txtInput.clear();

        }
    }

    public Color stringToColor(String string){
        Color color;
        try {
            Field field = Class.forName("java.awt.Color").getField(string);
            color = (Color)field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
        } return color;
    }

    public void draw(GraphicsContext graphicsContext){
        if(isStart) {
            while (true) {
                getDrawing = drawingService.sendPictureToPlayers();
                drawsAtributes = getDrawing.toString().split("#");
                double x = Double.parseDouble(drawsAtributes[0]);
                double y = Double.parseDouble(drawsAtributes[1]);
                ColorPicker colorPicker = new ColorPicker();
                colorPicker.setValue(stringToColor(drawsAtributes[2]));
                graphicsContext.beginPath();
                graphicsContext.moveTo(x, y);
                graphicsContext.setStroke(colorPicker.getValue());
                graphicsContext.stroke();
            }
        }
    }

}
