/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     ybonnel - initial API and implementation
 */
package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.LastUpdate;
import models.MessageTwitter;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class GetTwitters {

	private static final Logger LOGGER = Logger.getLogger(GetTwitters.class.getName());
	private String compte;

	public GetTwitters(String compte) {
		this.compte = compte;
	}


	private static TwitterFactory twitterFactory;

	private static synchronized TwitterFactory getFactory() {
		if (twitterFactory == null) {
			twitterFactory = new TwitterFactory();
		}
		return twitterFactory;
	}

	public Iterable<MessageTwitter> getMessages() {
		List<MessageTwitter> messages = new ArrayList<MessageTwitter>(20);
		if (LastUpdate.getInstance(compte).isUpdate()) {
			LOGGER.fine("Les messages twitter sont à jour, envoie du contenu de la base de donnée");
			messages.addAll(MessageTwitter.findByCompte(compte));
		} else {
			LOGGER.fine("Les messages twitter ne sont pas à jour, récupération du contenu de twiter");
			Twitter twitter = getFactory().getInstance();
			ResponseList<Status> listeStatus;
			try {
				listeStatus = twitter.getUserTimeline("@" + compte);
			} catch (TwitterException e) {
				LOGGER.log(Level.SEVERE, "Erreur lors de l'accès à twitter", e);
				return MessageTwitter.findByCompte(compte);
			}
			for (Status status : listeStatus) {
				messages.add(new MessageTwitter(status.getCreatedAt(), status.getText(), compte));
			}
			MessageTwitter.deleteAll();
			for (MessageTwitter message : messages) {
				message.save();
			}
		}
		Collections.sort(messages, new Comparator<MessageTwitter>() {
			public int compare(MessageTwitter o1, MessageTwitter o2) {
				return o2.getDateCreation().compareTo(o1.getDateCreation());
			}
		});
		return messages;
	}

}
