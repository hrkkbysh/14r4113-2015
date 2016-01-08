package controller;

public enum DebugModeScene  implements SceneType {
	MEDIAN("/ui/MedViewScene.fxml",false),
	SETTING("/ui/SimSettingScene.fxml",true),
	VLOG("/ui/LogScene.fxml",true),
	VL1("/ui/VisL1Scene.fxml",true),
	VL2("/ui/VisL2Scene.fxml",true);
	private String resource;
	private Boolean threadable;
	@Override
	public String getResource() {
		return resource;
	}

	@Override
	public Boolean isThreadable() {
		return threadable;
	}

	DebugModeScene(String resource,Boolean threadable) {
		this.resource = resource;
		this.threadable = threadable;
	}
}
