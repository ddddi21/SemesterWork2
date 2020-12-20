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


public class Draw  {
    public ColorPicker colorPicker;
    public javafx.scene.control.TextField txtName;
    public javafx.scene.control.TextField txtInput;
    javafx.scene.control.ScrollPane scrollPane;
    public javafx.scene.control.TextField roomField;
    public static TextArea txtAreaDisplay;
    public Connection connection;
    public GraphicsContext graphicsContext;
    SendDrawingService drawingService = new SendDrawingService();
    String[] drawsAtributes = new String[3];
    public Boolean isStart = false;
    public Integer id;

    public static Color convertHexToRgb(String colorStr) {
        colorStr = colorStr.substring(2);
        System.out.println("ConvertColor: " + colorStr);
        Color color = Color.rgb(Integer.valueOf(colorStr.substring(0, 2), 16),
                Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16));
        System.out.println("GetColor: "+ color.toString());
                return color;
    }

    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        VBox vBox = new VBox();
        scrollPane = new javafx.scene.control.ScrollPane();
        HBox hBox = new HBox();


        txtAreaDisplay = new TextArea();
        txtAreaDisplay.setEditable(false);
        scrollPane.setContent(txtAreaDisplay);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);


        final Canvas canvas = new Canvas(600, 300);
        graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);
        System.out.println("Draw connection: "+ connection);
        System.out.println("Draw socket: "+ connection.socket);
        drawingService.setSocket(connection.socket);
        //если игрок ведущий, то отрисовываем соответсующий юай и добавляем соотв функционал
        if(connection.isCommander) {
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(MouseEvent event) {
                            graphicsContext.beginPath();
                            double x = event.getX();
                            double y= event.getY();
                            graphicsContext.moveTo(x, y);
                            graphicsContext.setStroke(colorPicker.getValue());
                            graphicsContext.stroke();
                            isStart = true;
                            drawingService.isStartGame(isStart);
                            drawingService.receivePictureFromCommander(x,y,colorPicker.getValue());
                            drawingService.sendStartLine();
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
                            drawingService.sendPictureToPlayers();
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
                    clear();
                    drawingService.cleanBoard();
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
            Button btnSend = new Button("Send");
            btnSend.setOnAction(new ButtonListener());
            hBox.getChildren().addAll(txtName, txtInput, btnSend);
            HBox.setHgrow(txtInput, Priority.ALWAYS);
            vBox.getChildren().addAll(canvas, scrollPane, hBox);


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

    public void clear(){
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

    public void startDraw(GraphicsContext graphicsContext,String getDrawing){
        drawsAtributes = getDrawing.split("#");
        double x = Double.parseDouble(drawsAtributes[0]);
        double y = Double.parseDouble(drawsAtributes[1]);
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(convertHexToRgb(drawsAtributes[2]));
        graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.stroke();

    }

    public void draw(GraphicsContext graphicsContext,String getDrawing){
        System.out.println("Draw: Try to draw");
        if(isStart) {
                System.out.println("DrawsAtributes: " +getDrawing);
                drawsAtributes = getDrawing.split("#");
                double x = Double.parseDouble(drawsAtributes[0]);
                double y = Double.parseDouble(drawsAtributes[1]);
                ColorPicker colorPicker = new ColorPicker();
                colorPicker.setValue(convertHexToRgb(drawsAtributes[2]));
                graphicsContext.lineTo(x,y);
                graphicsContext.setStroke(colorPicker.getValue());
                graphicsContext.stroke();
        }
    }

    //слушатель на отправку сообщений в чат, который впоследствии отправляет другим клиентам сообщение
    public class ButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
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

}
