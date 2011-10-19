package controllers;

import play.mvc.Controller;
import utils.GetTwitters;
import utils.MessageTwitterJsonSerializer;

public class Application extends Controller {

    public static void index() {
        render();
    }

	public static void starbusmetro() {
		renderJSON(new GetTwitters("starbusmetro").getMessages(), MessageTwitterJsonSerializer.get());
	}

}