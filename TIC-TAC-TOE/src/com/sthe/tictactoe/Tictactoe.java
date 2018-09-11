//package com.sthe.tictactoe;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.input.MouseButton;
import javafx.animation.KeyValue;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.shape.Line;

import java.util.List;
import java.util.ArrayList;

public class Tictactoe extends Application
{
	private boolean playable = true;
	private boolean turnX = true;
	private Tile[][] board = new Tile[3][3];
	private List<Combos> combos = new ArrayList<>();
	private Pane root = new Pane();

	private Parent createContent()
	{
		root.setPrefSize(600, 600);

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				Tile tile = new Tile();
				tile.setTranslateX(j * 200);
				tile.setTranslateY(i * 200);

				root.getChildren().add(tile);

				board[j][i] = tile;
			}
		}

		//horizontal
		for (int y = 0; y < 3; y++)
		{
			combos.add(new Combos(board[0][y], board[1][y], board[2][y]));
		}

		//vertical
		for (int x = 0; x < 3; x++)
                {
                        combos.add(new Combos(board[x][0], board[x][1], board[x][2]));
                }

		//diagonal
		combos.add(new Combos(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combos(board[2][0], board[1][1], board[0][2]));


		return root;
	}

	private void checkState()
	{
		for (Combos combo: combos)
		{
			if (combo.isComplete())
			{
				playable = false;
				playWithAnimation(combo);
				break;
			}
		}
	}

	private void playWithAnimation(Combos combo)
	{
		Line line = new Line();
		line.setStartX(combo.tiles[0].getCenterX());
		line.setStartY(combo.tiles[0].getCenterY());
		line.setEndX(combo.tiles[0].getCenterX());
                line.setEndY(combo.tiles[0].getCenterY());

		root.getChildren().add(line);

		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2),
				       	new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
					new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play();
	}

	private class Combos
	{
		private Tile[] tiles;
		public Combos(Tile... tiles)
		{
			this.tiles = tiles;
		}

		public boolean isComplete()
		{
			if (tiles[0].getValue().isEmpty())
				return false;

			return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue());

		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.setTitle("Tic-tac-toe");
		primaryStage.show();
	}

	private class Tile extends StackPane
	{
		private Text text = new Text();

		public Tile()
		{
			Rectangle border = new Rectangle(200, 200);
			border.setFill(null);
			border.setStroke(Color.BLACK);

			text.setFont(Font.font(76));

			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);

			setOnMouseClicked(event -> 
			{
				if (!playable)
					return;
				if (event.getButton() == MouseButton.PRIMARY)
				{
					if (!turnX)
						return;
					drawX();
					turnX = false;
					checkState();
				}
				else if (event.getButton() == MouseButton.SECONDARY)
				{
					if (turnX)
						return;
					drawO();
					turnX = true;
					checkState();
				}
			});
		}

		public double getCenterX()
		{
			return getTranslateX() + 100;
		}

		public double getCenterY()
                {
                        return getTranslateY() + 100;
                }

		public String getValue()
		{
			return text.getText();
		}

		private void drawX()
		{
			text.setText("X");
		}

		private void drawO()
		{
			text.setText("O");
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
