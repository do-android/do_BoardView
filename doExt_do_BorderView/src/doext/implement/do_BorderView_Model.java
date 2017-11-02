package doext.implement;

import doext.define.do_BorderView_MAbstract;

/**
 * 自定义扩展组件Model实现，继承do_BorderLayout_MAbstract抽象类；
 *
 */
public class do_BorderView_Model extends do_BorderView_MAbstract {

	public do_BorderView_Model() throws Exception {
		super();
	}

	@Override
	public void didLoadView() throws Exception {
		super.didLoadView();
		((do_BorderView_View) this.getCurrentUIModuleView()).loadDefalutScriptFile();
	}

}
