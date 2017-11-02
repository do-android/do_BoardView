package doext.implement;

import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import core.DoServiceContainer;
import core.helper.DoUIModuleHelper.LayoutParamsType;
import core.interfaces.DoIPage;
import core.object.DoSourceFile;
import core.object.DoUIContainer;
import core.object.DoUIModule;

public class DoBorderView_CustomView {
    private DoUIContainer rootUiContainer;
    private String uiPath;
    private View currentView;
    private DoUIModule currentModel;
    private JSONObject childData;

    public DoBorderView_CustomView(String _uiPath, DoIPage _doPage) {
        if (!TextUtils.isEmpty(_uiPath)) {
            try {
                DoSourceFile _uiFile = _doPage.getCurrentApp().getSourceFS().getSourceByFileName(_uiPath);
                if (_uiFile != null) {
                    this.uiPath = _uiPath;
                    rootUiContainer = new DoUIContainer(_doPage);
                    rootUiContainer.loadFromFile(_uiFile, null, null);
                    if (null != _doPage.getScriptEngine()) {
                        rootUiContainer.loadDefalutScriptFile(uiPath);
                    }
                    currentModel = rootUiContainer.getRootView();
                    currentView = (View) currentModel.getCurrentUIModuleView();
                    currentModel.setLayoutParamsType(LayoutParamsType.RelativeLayout.toString());
                    // 设置headerView 的 宽高
                    currentView.setLayoutParams(new RelativeLayout.LayoutParams((int) currentModel.getRealWidth(), (int) currentModel.getRealHeight()));
                } else {
                    DoServiceContainer.getLogEngine().writeDebug("试图打开一个无效的页面文件:" + uiPath);
                }
            } catch (Exception e) {
                DoServiceContainer.getLogEngine().writeError("试图打开一个无效的页面文件:" + uiPath, e);
            }
        }
    }

    public View getView() {
        return this.currentView;
    }

    public String getUIModuleAddress() {
        if (null != currentModel) {
            return currentModel.getUniqueKey();
        }
        return null;
    }

    public double getUIRealHeight() {
        if (null != currentModel) {
            return currentModel.getRealHeight();
        }
        return 0;
    }

    public void dispose() {
        if (null != currentModel) {
            currentModel.dispose();
        }
    }

    public void loadDefalutScriptFile() throws Exception {
        if (rootUiContainer != null && uiPath != null) {
            rootUiContainer.loadDefalutScriptFile(uiPath);
            if (null != currentModel) {
                currentModel.didLoadView();
                currentModel.setModelData(childData);
            }
        }
    }

    public void setData(JSONObject _childData) throws Exception {
        this.childData = _childData;
        if (null != currentModel && rootUiContainer.getCurrentPage().getScriptEngine() != null) {
            currentModel.setModelData(_childData);
        }
    }

}
