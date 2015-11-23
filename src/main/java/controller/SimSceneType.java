package controller;

public enum SimSceneType implements SceneType {
	ROOT("/ui/RootScene.fxml",false),

	CASL2_EDIT("/ui/Casl2EditScene.fxml",true),
	RELOCATABLE("/ui/RelocatableScene.fxml", true),
	VISUALIZATION("/ui/VisualizationScene.fxml", true),
	COMET_INNERL2("/ui/Comet2InnerL2Scene.fxml", true),

	/*	TRACE("TraceScene.fxml"),
        PROFILE("ProfileScene.fxml"),*/
	COMET2_EDIT("/ui/Comet2EditScene.fxml",true);

	private String resource;
	private Boolean threadable;
	/* (非 Javadoc)
     * @see wadalab.edu.casl2sim.controller.SceneType#getResource()
     */
	@Override
	public String getResource() {
		return resource;
	}

	/* (非 Javadoc)
	 * @see wadalab.edu.casl2sim.controller.SceneType#isThreadable()
	 */
	@Override
	public Boolean isThreadable() {
		return threadable;
	}

	SimSceneType(String resource,Boolean threadable) {
		this.resource = resource;
		this.threadable = threadable;
	}
}