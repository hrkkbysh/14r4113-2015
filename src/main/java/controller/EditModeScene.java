package controller;

public enum EditModeScene implements SceneType {
	ROOT("/ui/RootScene.fxml",false),
	CASL2_EDIT("/ui/EditScene.fxml",true),
	DEBUG("/ui/DebuggerScene.fxml",true);

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

	EditModeScene(String resource,Boolean threadable) {
		this.resource = resource;
		this.threadable = threadable;
	}
}
