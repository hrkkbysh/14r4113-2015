package controller;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import javafx.fxml.FXMLLoader;

public class ReferenceController {
	
	public static void setReference(ScreensController<EditModeScene> sc,ExecutorService service){

		Map<EditModeScene, FXMLLoader> FXMLMap = sc.getFxmlLoaders();

		/*参照関係*/
		FXMLLoader froot = FXMLMap.get(EditModeScene.ROOT);
		RootController rootC = froot.getController();
		
		FXMLLoader fcaec = FXMLMap.get(EditModeScene.CASL2_EDIT);
		EditModeController caec = fcaec.getController();

		FXMLLoader fcoec = FXMLMap.get(EditModeScene.DEBUG);
		DebugModeController coec = fcoec.getController();
		
		rootC.setStage(sc.getStage());
		rootC.setCOEC(coec);

		caec.setCOEC(coec);

		coec.setCAEC(caec);
		
		for (EditModeScene sst : EditModeScene.values())
			if (sst.isThreadable()) {
				Threadable threadable = FXMLMap.get(sst).getController();
				threadable.setExecutorService(service);
			}
	}
}
