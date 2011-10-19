package controllers;

import play.mvc.Controller;
import utils.GetTwitters;
import utils.MessageTwitterJsonSerializer;

public class Application extends Controller {

	public static void starbusmetro() {
		renderJSON(new GetTwitters("starbusmetro").getMessages(), MessageTwitterJsonSerializer.get());
	}

	public static void tbc() {
		renderJSON(new GetTwitters("tbc").getMessages(), MessageTwitterJsonSerializer.get());
	}

}