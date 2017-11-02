package doext.define;

import core.object.DoProperty;
import core.object.DoProperty.PropertyDataType;
import core.object.DoUIModule;


public abstract class do_BorderView_MAbstract extends DoUIModule{

	protected do_BorderView_MAbstract() throws Exception {
		super();
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void onInit() throws Exception{
        super.onInit();
        //注册属性
		this.registProperty(new DoProperty("centerFillParent", PropertyDataType.Bool, "false", false));
		this.registProperty(new DoProperty("topView", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("rightView", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("bottomView", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("leftView", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("centerView", PropertyDataType.String, "", false));
		this.registProperty(new DoProperty("items", PropertyDataType.String, "", false));
	}
}