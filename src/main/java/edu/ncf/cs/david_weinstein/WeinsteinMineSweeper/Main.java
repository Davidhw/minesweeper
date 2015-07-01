package edu.ncf.cs.david_weinstein.WeinsteinMineSweeper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;

/**
 * Launch the spark server.
 * 
 * @author david weinstein
 */
public abstract class Main {
  Board board;

  /**
   * run server.
   */
  public static void main(String[] args) {
    runSparkServer();
  }

  /**
   * server error constant
   */
  private static final int INTERNAL_SERVER_ERROR = 500;

  /**
   * Prints server errors.
   */
  private static class ExceptionPrinter implements ExceptionHandler {

    /**
     * Prints server errors.
     */
    @Override
    public void handle(Exception exception, Request req, Response res) {
      res.status(INTERNAL_SERVER_ERROR);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        exception.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * Gives the location of templates and returns a freemarker engine.
   * 
   * @return a FreeMarker engine
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Sets up /play and /move.
   */
  private static void runSparkServer() {
    // We need to serve some simple static files containing CSS and JavaScript.
    // This tells Spark where to look for urls of the form "/static/*".
    Spark.externalStaticFileLocation("src/main/resources/static");

    // Development is easier if we show exceptions in the browser.
    Spark.exception(Exception.class, new ExceptionPrinter());

    // We render our responses with the FreeMaker template system.
    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/play", new NewGameHandler(), freeMarker);
    Spark.post("/move", new MoveHandler(), freeMarker);
  }

  /**
   * Starts a new game by returning a view of a title and board.
   */
  private static class NewGameHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Gson gson = new Gson();
      Board board = new Board();
      Map<String, Object> variables = ImmutableMap.of("title",
          "Minesweeper: Play Minesweeper!", "board", gson.toJson(board));
      return new ModelAndView(variables, "play.ftl");
    }
  }

  /**
   * Moves to the given position and returns the resulting board.
   */
  private static class MoveHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      // Board board = new Board(qm.value("board"));
      Gson gson = new Gson();
      Board board = gson.fromJson(qm.value("board"), Board.class);
      int inputRow = Integer.parseInt(qm.value("x"));
      int inputCol = Integer.parseInt(qm.value("y"));
      board.play(new GridLocation(inputRow, inputCol));
      Map<String, Object> variables = ImmutableMap.of("board",
          gson.toJson(board));
      return new ModelAndView(variables, "move.ftl");
    }
  }

}
