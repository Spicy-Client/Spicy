/*******************************************************************************
 * This file is part of Minebot.
 *
 * Minebot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Minebot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Minebot.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package net.famzangl.minecraft.minebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import java.net.URISyntaxException;

public class MinebotMod {

	
	static {

		// logging
		String doLogging = System.getProperty("MINEBOT_LOG", "0");
		if (doLogging.equals("1")) {
			LoggerContext context = (LoggerContext) LogManager.getContext(false);
			Configuration config = context.getConfiguration();
			try {
				context.setConfigLocation(MinebotMod.class.getResource("log4j.xml").toURI());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}


	}
	
	// Note: 6364136223846793005L * 0xc097ef87329e28a5l = 1



}
