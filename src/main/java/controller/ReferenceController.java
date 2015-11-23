package controller;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import javafx.fxml.FXMLLoader;

public class ReferenceController {
	
	public static final void setReference(ScreensController<SimSceneType> sc,ExecutorService service){
		AssemblerMode asmMode = AssemblerMode.NORMALCASL2;
		Map<SimSceneType, FXMLLoader> FXMLMap = sc.getFxmlLoaders();
		/*参照関係定義*/
		FXMLLoader froot = FXMLMap.get(SimSceneType.ROOT);
		RootController rootC = froot.getController();
		
		FXMLLoader fcaec = FXMLMap.get(SimSceneType.CASL2_EDIT);
		Casl2EditController caec = fcaec.getController();
		
		FXMLLoader frc = FXMLMap.get(SimSceneType.RELOCATABLE);
		RelocatableController rc = frc.getController();
		
		FXMLLoader fvc = FXMLMap.get(SimSceneType.VISUALIZATION);
		VisualizationController vc = fvc.getController();
		
		FXMLLoader fcoec = FXMLMap.get(SimSceneType.COMET2_EDIT);
		Comet2EditController coec = fcoec.getController();
		
		FXMLLoader fcic = FXMLMap.get(SimSceneType.COMET_INNERL2);
		Comet2InnerL2Controller cic = fcic.getController();
		
		rootC.setStage(sc.getStage());
		
		caec.setRC(rc);
		caec.setVC(vc);
		
		rc.setEC(caec);
		rc.setVC(vc);
		
		vc.setEC(caec);
		vc.setRC(rc);
		
		coec.setVC(vc);
		coec.setCEC(caec);
		
		cic.setVC(vc);
		
		for (SimSceneType sst : SimSceneType.values()) {
			if(sst.isThreadable()){
				Threadable threadable = (Threadable) FXMLMap.get(sst).getController();
				threadable.setExecutorService(service);
			}
			ModeTogglable modeTogglable = (ModeTogglable) FXMLMap.get(sst).getController();
			modeTogglable.setAssemblerMode(asmMode);
		}
	}
}
