package fr.bmartel.wlandecoder;

import java.util.ArrayList;

import fr.bmartel.protocol.wlan.frame.IWlanDataFrame;
import fr.bmartel.protocol.wlan.frame.IWlanManagementFrame;
import fr.bmartel.protocol.wlan.frame.control.inter.IClearToSendFrame;
import fr.bmartel.protocol.wlan.frame.control.inter.IContentionFreeFrame;
import fr.bmartel.protocol.wlan.frame.control.inter.IContentionFreeReceiveAckFrame;
import fr.bmartel.protocol.wlan.frame.control.inter.IPowerSavePollingFrame;
import fr.bmartel.protocol.wlan.frame.control.inter.IRequestToSendFrame;
import fr.bmartel.protocol.wlan.frame.control.inter.IackFrame;
import fr.bmartel.protocol.wlan.frame.data.inter.IDataFrame;
import fr.bmartel.protocol.wlan.frame.data.inter.INullFrame;
import fr.bmartel.protocol.wlan.frame.data.inter.IQosDataFrame;
import fr.bmartel.protocol.wlan.frame.management.DisassociationFrame;
import fr.bmartel.protocol.wlan.frame.management.element.IWlanElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.IDsssParameterSetElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.IErpElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.IExtendedSupportedRateElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.IHtCapabilitiesElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.ISsidElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.ISupportedRateElement;
import fr.bmartel.protocol.wlan.frame.management.element.inter.ITimElement;
import fr.bmartel.protocol.wlan.frame.management.inter.IAssociationRequestFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IAssociationResponseFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IAuthenticationFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IBeaconFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IDeauthenticationFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IProbeRequestFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IProbeResponseFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IReassociationRequestFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IReassociationResponseFrame;
import fr.bmartel.protocol.wlan.frame.management.inter.IibssAnnoucementIndicationMapFrame;
import fr.bmartel.protocol.wlan.inter.IWlan802dot11Radiotap;
import fr.bmartel.protocol.wlan.inter.IWlanControlFrame;
import fr.bmartel.protocol.wlan.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display decoding info on console
 * 
 * @author Bertrand Martel
 *
 */
public class DisplayDecodingInfo {

	private static final Logger logger = LoggerFactory.getLogger(DisplayDecodingInfo.class);
	
	/**
	 * Display all information about Wlan802dot11 frames
	 * 
	 * @param wlanDecodedFrameList
	 */
	public static void displayAllInfo(ArrayList<IWlan802dot11Radiotap> wlanDecodedFrameList)
	{
		for (int i = 0; i <wlanDecodedFrameList.size();i++)
		{
			logger.debug("Wlan frame control");
		
			logger.debug("    protocolVersion   : " + (wlanDecodedFrameList.get(i).getFrameControl().getProtocolVersion() & 0xFF));
			logger.debug("    type              : " + (wlanDecodedFrameList.get(i).getFrameControl().getType() & 0xFF));
			logger.debug("    subType           : " + (wlanDecodedFrameList.get(i).getFrameControl().getSubType() & 0xFF));
			logger.debug("    toDS              : " + wlanDecodedFrameList.get(i).getFrameControl().isToDS());
			logger.debug("    fromDS            : " + wlanDecodedFrameList.get(i).getFrameControl().isFromDS());
			logger.debug("    moreFragmentation : " + wlanDecodedFrameList.get(i).getFrameControl().isMoreFragmentation());
			logger.debug("    retry             : " + wlanDecodedFrameList.get(i).getFrameControl().isRetry());
			logger.debug("    powerManagement   : " + wlanDecodedFrameList.get(i).getFrameControl().isPowerManagement());
			logger.debug("    moreData          : " + wlanDecodedFrameList.get(i).getFrameControl().isMoreData());
			logger.debug("    wep               : " + wlanDecodedFrameList.get(i).getFrameControl().isWep());
			logger.debug("    order             : " + wlanDecodedFrameList.get(i).getFrameControl().isOrder());
			
			if (wlanDecodedFrameList.get(i).getFrame() instanceof IWlanManagementFrame)
			{
				logger.debug("Wlan management frame");
				IWlanManagementFrame managementFrame= (IWlanManagementFrame) wlanDecodedFrameList.get(i).getFrame();
				
				logger.debug("    duration id     : "+ ByteUtils.byteArrayToStringMessage("", managementFrame.getDurationId(), '|'));
				logger.debug("    destinationAddr : "+ ByteUtils.byteArrayToStringMessage("",managementFrame.getDestinationAddr(), '|'));
				logger.debug("    sourceAddr      : "+ ByteUtils.byteArrayToStringMessage("", managementFrame.getSourceAddr(), '|'));
				logger.debug("    bssid           : "+ ByteUtils.byteArrayToStringMessage("", managementFrame.getBSSID(), '|'));
				logger.debug("    sequenceControl : "+ ByteUtils.byteArrayToStringMessage("",managementFrame.getSequenceControl(), '|'));
				logger.debug("    frameBody       : "+ ByteUtils.byteArrayToStringMessage("",managementFrame.getFrameBody(), '|'));
				logger.debug("    fcs             : "+ ByteUtils.byteArrayToStringMessage("",managementFrame.getFcs(), '|'));
				
				if (wlanDecodedFrameList.get(i).getFrame() instanceof IAssociationRequestFrame)
				{
					logger.debug("    MANAGEMENT ASSOCIATION REQUEST FRAME");
					
					IAssociationRequestFrame associationRequestFrame =(IAssociationRequestFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("        listenInterval         : "+ ByteUtils.byteArrayToStringMessage("", associationRequestFrame.getListenInterval(),'|'));
					logger.debug("        capability information : "+ ByteUtils.byteArrayToStringMessage("", associationRequestFrame.getCapabilityInfo(),'|'));

					for (int j = 0; j< associationRequestFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(associationRequestFrame.getTaggedParameter().get(j));
					}
				}
				else if(wlanDecodedFrameList.get(i).getFrame() instanceof IAssociationResponseFrame)
				{
					logger.debug("    MANAGEMENT ASSOCATION RESPONSE FRAME");
					
					IAssociationResponseFrame associationFrame =(IAssociationResponseFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("        statusCode             : " + associationFrame.getStatusCode());
					logger.debug("        associationId          : "+ associationFrame.getAssociationId());
					logger.debug("        capability information : "+ ByteUtils.byteArrayToStringMessage("", associationFrame.getCapabilityInfo(),'|'));
					
					for (int j = 0; j< associationFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(associationFrame.getTaggedParameter().get(j));
					}
				}
				else if(wlanDecodedFrameList.get(i).getFrame() instanceof IAuthenticationFrame)
				{
					logger.debug("    MANAGEMENT AUTHENTICATION FRAME");
					
					IAuthenticationFrame authenticationFrame =(IAuthenticationFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("        authenticationAlgorithmNum : "+ authenticationFrame.getAuthenticationAlgorithmNum());
					logger.debug("        authenticationSeqNum       : "+ authenticationFrame.getAuthenticationSeqNum());
					logger.debug("        statusCode                 : " + authenticationFrame.getStatusCode());
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IBeaconFrame)
				{
					logger.debug("    MANAGEMENT BEACON FRAME");
					
					IBeaconFrame beaconFrame =(IBeaconFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("        timestamp              : "+ ByteUtils.byteArrayToStringMessage("",beaconFrame.getTimestamp(), '|'));
					logger.debug("        beaconInterval         : "+ ByteUtils.convertByteArrayToInt(beaconFrame.getBeaconInterval()));
					logger.debug("        capability information : "+ ByteUtils.byteArrayToStringMessage("", beaconFrame.getCapabilityInfo(),'|'));
					
					for (int j = 0; j< beaconFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(beaconFrame.getTaggedParameter().get(j));
					}
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IDeauthenticationFrame)
				{
					logger.debug("    MANAGEMENT DEAUTHENTICATION FRAME");
					
					logger.debug("        reasonCode : " + ((IDeauthenticationFrame)wlanDecodedFrameList.get(i).getFrame()).getReasonCode());
				}
				else if(wlanDecodedFrameList.get(i).getFrame() instanceof DisassociationFrame)
				{
					logger.debug("    MANAGEMENT DISASSOCIATION FRAME");
					
					logger.debug("        statusCode : " + ((DisassociationFrame)wlanDecodedFrameList.get(i).getFrame()).getStatusCode());
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IibssAnnoucementIndicationMapFrame)
				{
					logger.debug("    MANAGEMENT IBSS ANNOUCEMENT INDICATION MAP FRAME");
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IProbeRequestFrame)
				{
					logger.debug("    MANAGEMENT - PROBE REQUEST FRAME");
					IProbeRequestFrame probeFrame =(IProbeRequestFrame) wlanDecodedFrameList.get(i).getFrame();
					
					for (int j = 0; j< probeFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(probeFrame.getTaggedParameter().get(j));
					}
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IProbeResponseFrame)
				{
					IProbeResponseFrame probeFrame =(IProbeResponseFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("    MANAGEMENT - PROBE RESPONSE FRAME");
					logger.debug("        timestamp              : "+ ByteUtils.byteArrayToStringMessage("",probeFrame.getTimestamp(), '|'));
					logger.debug("        beaconInterval         : "+ ByteUtils.convertByteArrayToInt(probeFrame.getBeaconInterval()));
					logger.debug("        capability information : "+ ByteUtils.byteArrayToStringMessage("", probeFrame.getCapabilityInformation(),'|'));
					
					for (int j = 0; j< probeFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(probeFrame.getTaggedParameter().get(j));
					}
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IReassociationRequestFrame)
				{
					IReassociationRequestFrame reassociationFrame =(IReassociationRequestFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("    MANAGEMENT REASSOCIATION FRAME");
					
					logger.debug("        listenInterval         : "+ ByteUtils.byteArrayToStringMessage("", reassociationFrame.getListenInterval(),'|'));
					logger.debug("        currentAPAdress        : "+ ByteUtils.convertByteArrayToInt(reassociationFrame.getCurrentAPAdress()));
					logger.debug("        capability information : "+ ByteUtils.byteArrayToStringMessage("", reassociationFrame.getCapabilityInfo(),'|'));
					
					for (int j = 0; j< reassociationFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(reassociationFrame.getTaggedParameter().get(j));
					}
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IReassociationResponseFrame)
				{
					IReassociationResponseFrame reassociationFrame =(IReassociationResponseFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("    MANAGEMENT REASSOCATION RESPONSE FRAME");

					logger.debug("        statusCode             : " + reassociationFrame.getStatusCode());
					logger.debug("        associationId          : "+ reassociationFrame.getAssociationId());
					logger.debug("        capability information : "+ ByteUtils.byteArrayToStringMessage("", reassociationFrame.getCapabilityInfo(),'|'));
					
					for (int j = 0; j< reassociationFrame.getTaggedParameter().size(); j++) {
						displayTaggedParameter(reassociationFrame.getTaggedParameter().get(j));
					}
				}
			}
			if (wlanDecodedFrameList.get(i).getFrame() instanceof IWlanDataFrame)
			{
				logger.debug("Wlan data frame");
				IWlanDataFrame dataFrame= (IWlanDataFrame) wlanDecodedFrameList.get(i).getFrame();
				
				logger.debug("    duration id     : "+ ByteUtils.byteArrayToStringMessage("", dataFrame.getDurationId(), '|'));
				logger.debug("    destinationAddr : "+ ByteUtils.byteArrayToStringMessage("",dataFrame.getDestinationAddr(), '|'));
				logger.debug("    sourceAddr      : "+ ByteUtils.byteArrayToStringMessage("", dataFrame.getSourceAddr(), '|'));
				logger.debug("    bssid           : "+ ByteUtils.byteArrayToStringMessage("", dataFrame.getBSSID(), '|'));
				logger.debug("    sequenceControl : "+ ByteUtils.byteArrayToStringMessage("",dataFrame.getSequenceControl(), '|'));
				logger.debug("    frameBody       : "+ ByteUtils.byteArrayToStringMessage("",dataFrame.getFrameBody(), '|'));
				
				if (wlanDecodedFrameList.get(i).getFrame() instanceof IDataFrame)
					logger.debug("    DATA FRAME");
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof INullFrame)
					logger.debug("    NULL FRAME");
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IQosDataFrame)
				{
					logger.debug("    QOS DATA FRAME");
					logger.debug("        qos control : "+ ByteUtils.byteArrayToStringMessage("", ((IQosDataFrame)wlanDecodedFrameList.get(i).getFrame()).getQosControl(), '|'));
				}		
			}
			if (wlanDecodedFrameList.get(i).getFrame() instanceof IWlanControlFrame)
			{
				
				logger.debug("Wlan control frame");
				
				if (wlanDecodedFrameList.get(i).getFrame() instanceof IackFrame)
				{
					IackFrame ackFrame =(IackFrame) wlanDecodedFrameList.get(i).getFrame();
					logger.debug("    CONTROL FRAME - ACK PACKET");
					logger.debug("        duration id  : "+ ByteUtils.byteArrayToStringMessage("", ackFrame.getDurationId(), '|'));
					logger.debug("        receiverAddr : "+ ByteUtils.byteArrayToStringMessage("", ackFrame.getReceiverAddr(),'|'));
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IClearToSendFrame)
				{
					IClearToSendFrame cts =(IClearToSendFrame) wlanDecodedFrameList.get(i).getFrame();
					logger.debug("    CONTROL FRAME - CLEAR TO SEND PACKET");
					logger.debug("        duration id  : "+ ByteUtils.byteArrayToStringMessage("", cts.getDurationId(), '|'));
					logger.debug("        receiverAddr : "+ ByteUtils.byteArrayToStringMessage("", cts.getReceiverAddr(),'|'));
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IContentionFreeFrame)
				{
					IContentionFreeFrame contentionFr =(IContentionFreeFrame) wlanDecodedFrameList.get(i).getFrame();
					logger.debug("    CONTROL FRAME - CONTENTION FREE PACKET");
					logger.debug("        duration id : "+ ByteUtils.byteArrayToStringMessage("", contentionFr.getDurationId(), '|'));
					logger.debug("        receiverAddr : "+ ByteUtils.byteArrayToStringMessage("", contentionFr.getReceiverAddr(),'|'));
					logger.debug("        bss id       : "+ ByteUtils.byteArrayToStringMessage("", contentionFr.getBssid(), '|'));
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IContentionFreeReceiveAckFrame)
				{
					IContentionFreeReceiveAckFrame contentionFr =(IContentionFreeReceiveAckFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("    CONTROL FRAME - CONTENTION FREE AND RECEIVE ACK PACKET");
					logger.debug("        duration id  : "+ ByteUtils.byteArrayToStringMessage("", contentionFr.getDurationId(), '|'));
					logger.debug("        receiverAddr : "+ ByteUtils.byteArrayToStringMessage("", contentionFr.getReceiverAddr(),'|'));
					logger.debug("        bss id       : "+ ByteUtils.byteArrayToStringMessage("", contentionFr.getBssid(), '|'));
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IPowerSavePollingFrame)
				{
					IPowerSavePollingFrame powerSave =(IPowerSavePollingFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("    CONTROL FRAME - POWER SAVE POLLING PACKET");
					logger.debug("        association id : "+ ByteUtils.byteArrayToStringMessage("", powerSave.getAssociationId(),'|'));
					logger.debug("        bss id         : "+ ByteUtils.byteArrayToStringMessage("", powerSave.getBssid(), '|'));
					logger.debug("        transmitter id : "+ ByteUtils.byteArrayToStringMessage("", powerSave.getTransmitterId(),'|'));
				}
				else if (wlanDecodedFrameList.get(i).getFrame() instanceof IRequestToSendFrame)
				{
					IRequestToSendFrame rts =(IRequestToSendFrame) wlanDecodedFrameList.get(i).getFrame();
					
					logger.debug("    CONTROL FRAME - REQUEST TO SEND PACKET");
					logger.debug("        duration id     : "+ ByteUtils.byteArrayToStringMessage("", rts.getDurationId(), '|'));
					logger.debug("        receiverAddr    : "+ ByteUtils.byteArrayToStringMessage("", rts.getReceiverAddr(),'|'));
					logger.debug("        transmitterAddr : "+ ByteUtils.byteArrayToStringMessage("",rts.getTransmitterAddr(), '|'));
				}
			}
			
		}
	}
	
	/**
	 * Display element information
	 * 
	 * @param elementList
	 * 		List of wlan element 
	 */
	public static void displayTaggedParameter(IWlanElement elementList)
	{
		logger.debug("            Dynamic Elements");
		logger.debug("            element id : "+ (elementList.getElementId() & 0xFF));
		
		if (elementList instanceof IDsssParameterSetElement)
		{
			IDsssParameterSetElement element = (IDsssParameterSetElement)elementList;
			logger.debug("                currentChannel : "+ element.getCurrentChannel());
			logger.debug("                frequency      : " + element.getFrequency());
		}
		else if (elementList instanceof IErpElement)
		{
			IErpElement element = (IErpElement)elementList;
			logger.debug("                isErpPresent         : " + element.isErpPresent());
			logger.debug("                useProtection        : " + element.useProtection());
			logger.debug("                isBarkerPreambleMode : " + element.isBarkerPreambleMode());
		}
		else if (elementList instanceof IExtendedSupportedRateElement)
		{
			IExtendedSupportedRateElement element = (IExtendedSupportedRateElement)elementList;
			String dataRateStr = "";
			for (int i = 0; i < element.getDataRate().length; i++) {
				dataRateStr += ((element.getDataRate()[i] & 0x7F) * 500) + "kbps,";
			}
			logger.debug("                extended data rates supported : " + dataRateStr);
		}
		else if (elementList instanceof IHtCapabilitiesElement)
		{
			IHtCapabilitiesElement element = (IHtCapabilitiesElement)elementList;
			logger.debug("                support short 20Mhz           : "+ element.getHtCapabilityInfo().isSupportShortGi20Mhz());
			logger.debug("                support short 40Mhz           : "+ element.getHtCapabilityInfo().isSupportShortGi40Mhz());

			logger.debug("                channel width 20MHz supported : "+ !element.getHtCapabilityInfo().isSupportedChannelWidthSet());
			logger.debug("                channel width 40MHz supported : "+ element.getHtCapabilityInfo().isSupportedChannelWidthSet());
			
			logger.debug("                AMPDU paramters               : "	+ (element.getAmpduParameters() & 0xFF));
			
			for (int i = 0; i < element.getSupportedMCSSet().getMcsList().size(); i++) {
				logger.debug("                MCS Supported : n°"+ element.getSupportedMCSSet().getMcsList().get(i).getMcsIndex() + "-"+ element.getSupportedMCSSet().getMcsList().get(i).getModulation());
			}
			
			logger.debug("                HT Extended capabilities           : "+ ByteUtils.byteArrayToStringMessage("",element.getHtExtendedCapabilities(), '|'));
			logger.debug("                Transmit beam forming capabilities : "+ ByteUtils.byteArrayToStringMessage("",element.getTransmitBeamformingCapabilities(), '|'));
			logger.debug("                ASEL capabilities                  : "+ (element.getAselCapabilities() & 0xFF));
		}
		else if (elementList instanceof ISsidElement)
		{
			logger.debug("                SSID : " +((ISsidElement)elementList).getSsid());
		}
		else if (elementList instanceof ISupportedRateElement)
		{
			ISupportedRateElement element = (ISupportedRateElement)elementList;
			String dataRateStr = "";
			
			for (int i = 0; i < element.getDataRate().length; i++) {
				dataRateStr += ((element.getDataRate()[i] & 0x7F) * 500) + "kbps,";
			}
			
			logger.debug("                data rates supported : "+ dataRateStr);
		}
		else if (elementList instanceof ITimElement)
		{
			ITimElement element = (ITimElement)elementList;
			logger.debug("                number of beacon frame before next DTIM  : "+ (element.getDTIMcount() & 0xFF));
			logger.debug("                number of beacon interval between 2 DTIM : "+ (element.getDTIMperiod() & 0XFF));
			logger.debug("                TIM bitmapControl                        : "+ (element.getBitmapControl() & 0XFF));
			logger.debug(ByteUtils.byteArrayToStringMessage("                TIM partialVirtualBitmap", element.getPartialVirtualBitmap(), '|'));;
		}
	}
	
}
