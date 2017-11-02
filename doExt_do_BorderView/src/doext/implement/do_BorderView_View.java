package doext.implement;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import core.DoServiceContainer;
import core.helper.DoJsonHelper;
import core.helper.DoTextHelper;
import core.helper.DoUIModuleHelper;
import core.interfaces.DoIScriptEngine;
import core.interfaces.DoIUIModuleView;
import core.object.DoInvokeResult;
import core.object.DoUIModule;
import doext.define.do_BorderView_IMethod;
import doext.define.do_BorderView_MAbstract;

/**
 * 自定义扩展UIView组件实现类，此类必须继承相应VIEW类，并实现DoIUIModuleView,do_BorderLayout_IMethod接口；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.model.getUniqueKey());
 */
public class do_BorderView_View extends RelativeLayout implements DoIUIModuleView, do_BorderView_IMethod {

	private boolean centerFillParent;

	private DoBorderView_CustomView topView;
	private DoBorderView_CustomView rightView;
	private DoBorderView_CustomView bottomView;
	private DoBorderView_CustomView leftView;
	private DoBorderView_CustomView centerView;

	/**
	 * 每个UIview都会引用一个具体的model实例；
	 */
	private do_BorderView_MAbstract model;

	public do_BorderView_View(Context context) {
		super(context);
	}

	/**
	 * 初始化加载view准备,_doUIModule是对应当前UIView的model实例
	 */
	@Override
	public void loadView(DoUIModule _doUIModule) throws Exception {
		this.model = (do_BorderView_MAbstract) _doUIModule;

	}

	/**
	 * 动态修改属性值时会被调用，方法返回值为true表示赋值有效，并执行onPropertiesChanged，否则不进行赋值；
	 * 
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public boolean onPropertiesChanging(Map<String, String> _changedValues) {
		return true;
	}

	/**
	 * 属性赋值成功后被调用，可以根据组件定义相关属性值修改UIView可视化操作；
	 * 
	 * @_changedValues<key,value>属性集（key名称、value值）；
	 */
	@Override
	public void onPropertiesChanged(Map<String, String> _changedValues) {
		DoUIModuleHelper.handleBasicViewProperChanged(this.model, _changedValues);
		if (_changedValues.containsKey("centerFillParent")) {
			this.centerFillParent = DoTextHelper.strToBool(_changedValues.get("centerFillParent"), false);
		}
		if (_changedValues.containsKey("topView")) {
			removeFromSuperview(topView);
			topView = new DoBorderView_CustomView(_changedValues.get("topView"), model.getCurrentPage());
		}
		if (_changedValues.containsKey("rightView")) {
			removeFromSuperview(rightView);
			rightView = new DoBorderView_CustomView(_changedValues.get("rightView"), model.getCurrentPage());
		}
		if (_changedValues.containsKey("bottomView")) {
			removeFromSuperview(bottomView);
			bottomView = new DoBorderView_CustomView(_changedValues.get("bottomView"), model.getCurrentPage());
		}
		if (_changedValues.containsKey("leftView")) {
			removeFromSuperview(leftView);
			leftView = new DoBorderView_CustomView(_changedValues.get("leftView"), model.getCurrentPage());
		}
		if (_changedValues.containsKey("centerView")) {
			removeFromSuperview(centerView);
			centerView = new DoBorderView_CustomView(_changedValues.get("centerView"), model.getCurrentPage());
		}

		if (_changedValues.containsKey("items")) {
			try {
				String _address = _changedValues.get("items");
				bindItems(_address);
			} catch (Exception _err) {
				DoServiceContainer.getLogEngine().writeError("do_BorderView items", _err);
			}
		}
	}

	private void bindItems(String _itmes) throws Exception {
		if (!TextUtils.isEmpty(_itmes)) {
			JSONObject _data = new JSONObject(_itmes);
			if (topView != null && _data.has("topView")) {
				topView.setData(_data.getJSONObject("topView"));
			}
			if (rightView != null && _data.has("rightView")) {
				rightView.setData(_data.getJSONObject("rightView"));
			}
			if (bottomView != null && _data.has("bottomView")) {
				bottomView.setData(_data.getJSONObject("bottomView"));
			}
			if (leftView != null && _data.has("leftView")) {
				leftView.setData(_data.getJSONObject("leftView"));
			}
			if (centerView != null && _data.has("centerView")) {
				centerView.setData(_data.getJSONObject("centerView"));
			}
		}
	}

	private void removeFromSuperview(DoBorderView_CustomView _view) {
		if (_view != null) {
			View mView = _view.getView();
			if (mView != null) {
				DoUIModuleHelper.hideKeyboard(mView);
				DoUIModuleHelper.removeFromSuperview(mView);
				_view.dispose();
			}
		}
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("getView".equals(_methodName)) {
			this.getView(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		return false;
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.model.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		return false;
	}

	/**
	 * 释放资源处理，前端JS脚本调用closePage或执行removeui时会被调用；
	 */
	@Override
	public void onDispose() {

	}

	/**
	 * 重绘组件，构造组件时由系统框架自动调用；
	 * 或者由前端JS脚本调用组件onRedraw方法时被调用（注：通常是需要动态改变组件（X、Y、Width、Height）属性时手动调用）
	 */
	@Override
	public void onRedraw() {
		this.setLayoutParams(DoUIModuleHelper.getLayoutParams(this.model));
		if (topView != null) {
			View mView = topView.getView();
			if (mView != null) {
				mView.setId(0x00001000);
				RelativeLayout.LayoutParams rlp = (LayoutParams) mView.getLayoutParams();
				rlp.addRule(ALIGN_PARENT_TOP);
				DoUIModuleHelper.removeFromSuperview(mView);
				this.addView(mView);
			}
		}

		if (rightView != null) {
			View mView = rightView.getView();
			if (mView != null) {
				mView.setId(0x00001001);
				RelativeLayout.LayoutParams rlp = (LayoutParams) mView.getLayoutParams();
				rlp.addRule(BELOW, 0x00001000);
				rlp.addRule(ALIGN_PARENT_RIGHT);
				DoUIModuleHelper.removeFromSuperview(mView);
				this.addView(mView);
			}
		}
		if (bottomView != null) {
			View mView = bottomView.getView();
			if (mView != null) {
				mView.setId(0x00001002);
				RelativeLayout.LayoutParams rlp = (LayoutParams) mView.getLayoutParams();
				rlp.addRule(ALIGN_PARENT_BOTTOM);
				DoUIModuleHelper.removeFromSuperview(mView);
				this.addView(mView);
			}
		}
		if (leftView != null) {
			View mView = leftView.getView();
			if (mView != null) {
				mView.setId(0x00001003);
				RelativeLayout.LayoutParams rlp = (LayoutParams) mView.getLayoutParams();
				rlp.addRule(BELOW, 0x00001000);
				rlp.addRule(ALIGN_PARENT_LEFT);
				DoUIModuleHelper.removeFromSuperview(mView);
				this.addView(mView);
			}
		}
		if (centerView != null) {
			View mView = centerView.getView();
			if (mView != null) {
				mView.setId(0x00001004);
				RelativeLayout.LayoutParams rlp = (LayoutParams) mView.getLayoutParams();
				if (centerFillParent) {
					rlp.addRule(BELOW, 0x00001000);
					if (bottomView == null) { //无法确定bottomView的高度，须取leftView和rightView高度的最大值
						rlp.height = getCenterViewHight();
					} else {
						rlp.addRule(ABOVE, 0x00001002);
					}

					if (rightView == null) {
						rlp.width = -1;
					} else {
						rlp.addRule(LEFT_OF, 0x00001001);
					}

					if (leftView == null) {
						rlp.width = -1;
					} else {
						rlp.addRule(RIGHT_OF, 0x00001003);

					}

					//如果borderView 高度不为-1，并且 leftView 或rightView 有一个有高度
					if ((leftView != null || rightView != null) && !isHight()) {
						rlp.height = -1;
					}
				} else {
					rlp.addRule(CENTER_IN_PARENT);
				}
				DoUIModuleHelper.removeFromSuperview(mView);
				this.addView(mView);
			}
		}

	}

	private boolean isHight() {
		try {
			return "-1".equals(model.getPropertyValue("hight"));
		} catch (Exception e) {

		}
		return false;
	}

	private int getCenterViewHight() {

		int leftViewHeight = -1;
		int rightViewHeight = -1;

		if (leftView != null) {
			leftViewHeight = (int) leftView.getUIRealHeight();
		}

		if (rightView != null) {
			rightViewHeight = (int) rightView.getUIRealHeight();
		}

		return leftViewHeight > rightViewHeight ? leftViewHeight : rightViewHeight;
	}

	public void loadDefalutScriptFile() throws Exception {
		if (topView != null) {
			topView.loadDefalutScriptFile();
		}
		if (rightView != null) {
			rightView.loadDefalutScriptFile();
		}
		if (bottomView != null) {
			bottomView.loadDefalutScriptFile();
		}
		if (leftView != null) {
			leftView.loadDefalutScriptFile();
		}
		if (centerView != null) {
			centerView.loadDefalutScriptFile();
		}
	}

	//获取当前组件内所有第一层子view的id
	private void getView(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String _direction = DoJsonHelper.getString(_dictParas, "direction", "");
		if (TextUtils.isEmpty(_direction)) {
			throw new Exception("direction 参数值不能为空！");
		}

		String _moduleAddress = null;
		if ("top".equalsIgnoreCase(_direction)) {
			if (topView != null) {
				_moduleAddress = topView.getUIModuleAddress();
			}
		} else if ("right".equalsIgnoreCase(_direction)) {
			if (rightView != null) {
				_moduleAddress = rightView.getUIModuleAddress();
			}

		} else if ("bottom".equalsIgnoreCase(_direction)) {
			if (bottomView != null) {
				_moduleAddress = bottomView.getUIModuleAddress();
			}
		} else if ("left".equalsIgnoreCase(_direction)) {
			if (leftView != null) {
				_moduleAddress = leftView.getUIModuleAddress();
			}
		} else if ("center".equalsIgnoreCase(_direction)) {
			if (centerView != null) {
				_moduleAddress = centerView.getUIModuleAddress();
			}
		} else {
			throw new Exception("direction 参数值非法！");
		}

		_invokeResult.setResultText(_moduleAddress);
	}

	/**
	 * 获取当前model实例
	 */
	@Override
	public DoUIModule getModel() {
		return model;
	}
}