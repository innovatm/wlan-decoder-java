package fr.bmartel.protocol.wlan.frame.management.element.impl;

import fr.bmartel.protocol.wlan.frame.management.element.WlanElementAbstr;
import fr.bmartel.protocol.wlan.frame.management.element.inter.ISupportedRateElement;
import fr.bmartel.wlandecoder.DisplayDecodingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Define data rate<br/>
 * <ul>
 * <li>element id : 1 Byte</li>
 * <li>length : 1 Byte</li>
 * <li>data : 1 - 8 Bytes</li>
 * </ul>
 * 
 * @author Bertrand Martel
 * 
 */
public class SupportedRateElement extends WlanElementAbstr implements ISupportedRateElement{

	private static final Logger logger = LoggerFactory.getLogger(SupportedRateElement.class);

	public static final int id = 1;

	private byte[] dataRate = null;

	public SupportedRateElement(byte[] data) {
		super(data);
		dataRate = data;
	}

	/**
	 * retrieve max rate in Mbps
	 * 
	 * @return
	 */
	public int getMaxRate() {
		if (dataRate.length > 0) {
			logger.debug(String.valueOf((dataRate[dataRate.length - 1] & 0x7F) * 500));
			return ((dataRate[dataRate.length - 1] & 0x7F) * 500) / 1000;
		} else {
			return -1;
		}
	}

	@Override
	public byte getElementId() {
		return id;
	}

	@Override
	public byte[] getDataRate() {
		return dataRate;
	}
}
