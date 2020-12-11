import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Test extends Application {
    ColorPicker colorPicker;

    @Override
    public void start(Stage primaryStage) {

        final Canvas canvas = new Canvas(400, 400);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.setStroke(colorPicker.getValue());
                        graphicsContext.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.setStroke(colorPicker.getValue());
                        graphicsContext.stroke();
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {

                    }
                });

        Group root = new Group();

        Button clean = new Button();
        Button eraser = new Button();

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

//        eraser.setText("eraser");
//        eraser.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                double canvasWidth = graphicsContext.getCanvas().getWidth();
//                double canvasHeight = graphicsContext.getCanvas().getHeight();
//
//                graphicsContext.setStroke(Color.BLACK);
//                graphicsContext.setLineWidth(5);
//                graphicsContext.fill();
//                graphicsContext.strokeRect(
//                        0,              //x of the upper left corner
//                        0,              //y of the upper left corner
//                        canvasWidth,    //width of the rectangle
//                        canvasHeight);  //height of the rectangle
//
//
//            }
//        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(colorPicker, clean, canvas);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, 500, 525);
        primaryStage.setTitle("крокодильчик");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
}