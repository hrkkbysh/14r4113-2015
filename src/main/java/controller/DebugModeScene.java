package controller;

public enum DebugModeScene  implements SceneType {
	VL1("/ui/VisL1Scene.fxml",true),
	VL2("/ui/VisL2Scene.fxml",true),
	LOG("/ui/LogScene.fxml",true),
	LOAD("/ui/LoadScene.fxml",false),
	SETTING("/ui/SimSettingScene.fxml",true);
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
