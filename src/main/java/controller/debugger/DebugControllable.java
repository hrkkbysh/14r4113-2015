package controller.debugger;

import comet2casl2.MachineObserver;
import controller.DebugModeController;

/**
 * @author 14r4113 on 2016/01/13.
 */
public interface DebugControllable {
	void initState(MachineObserver binder);
	void setRoot(DebugModeController c);
}