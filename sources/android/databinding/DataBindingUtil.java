package android.databinding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DataBindingUtil {
    private static DataBindingComponent sDefaultComponent = null;
    private static DataBinderMapper sMapper = new DataBinderMapperImpl();

    private DataBindingUtil() {
    }

    public static void setDefaultComponent(DataBindingComponent bindingComponent) {
        sDefaultComponent = bindingComponent;
    }

    public static DataBindingComponent getDefaultComponent() {
        return sDefaultComponent;
    }

    public static <T extends ViewDataBinding> T inflate(LayoutInflater inflater, int layoutId, ViewGroup parent, boolean attachToParent) {
        return inflate(inflater, layoutId, parent, attachToParent, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T inflate(LayoutInflater inflater, int layoutId, ViewGroup parent, boolean attachToParent, DataBindingComponent bindingComponent) {
        int startChildren = 0;
        boolean useChildren = parent != null && attachToParent;
        if (useChildren) {
            startChildren = parent.getChildCount();
        }
        View view = inflater.inflate(layoutId, parent, attachToParent);
        if (useChildren) {
            return bindToAddedViews(bindingComponent, parent, startChildren, layoutId);
        }
        return bind(bindingComponent, view, layoutId);
    }

    public static <T extends ViewDataBinding> T bind(View root) {
        return bind(root, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T bind(View root, DataBindingComponent bindingComponent) {
        T binding = getBinding(root);
        if (binding != null) {
            return binding;
        }
        Object tagObj = root.getTag();
        if (tagObj instanceof String) {
            int layoutId = sMapper.getLayoutId((String) tagObj);
            if (layoutId != 0) {
                return sMapper.getDataBinder(bindingComponent, root, layoutId);
            }
            throw new IllegalArgumentException("View is not a binding layout. Tag: " + tagObj);
        }
        throw new IllegalArgumentException("View is not a binding layout");
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent bindingComponent, View[] roots, int layoutId) {
        return sMapper.getDataBinder(bindingComponent, roots, layoutId);
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent bindingComponent, View root, int layoutId) {
        return sMapper.getDataBinder(bindingComponent, root, layoutId);
    }

    /* JADX WARNING: type inference failed for: r0v1, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T extends android.databinding.ViewDataBinding> T findBinding(android.view.View r12) {
        /*
        L_0x0000:
            r0 = 0
            if (r12 == 0) goto L_0x005d
            android.databinding.ViewDataBinding r1 = android.databinding.ViewDataBinding.getBinding(r12)
            if (r1 == 0) goto L_0x000a
            return r1
        L_0x000a:
            java.lang.Object r2 = r12.getTag()
            boolean r3 = r2 instanceof java.lang.String
            if (r3 == 0) goto L_0x004f
            r3 = r2
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "layout"
            boolean r4 = r3.startsWith(r4)
            if (r4 == 0) goto L_0x004f
            java.lang.String r4 = "_0"
            boolean r4 = r3.endsWith(r4)
            if (r4 == 0) goto L_0x004f
            r4 = 6
            char r4 = r3.charAt(r4)
            r5 = 7
            r6 = 47
            int r5 = r3.indexOf(r6, r5)
            r7 = 0
            r8 = 0
            r9 = 1
            r10 = -1
            if (r4 != r6) goto L_0x003c
            if (r5 != r10) goto L_0x003a
            r8 = r9
        L_0x003a:
            r7 = r8
            goto L_0x004c
        L_0x003c:
            r11 = 45
            if (r4 != r11) goto L_0x004c
            if (r5 == r10) goto L_0x004c
            int r11 = r5 + 1
            int r6 = r3.indexOf(r6, r11)
            if (r6 != r10) goto L_0x004b
            r8 = r9
        L_0x004b:
            r7 = r8
        L_0x004c:
            if (r7 == 0) goto L_0x004f
            return r0
        L_0x004f:
            android.view.ViewParent r0 = r12.getParent()
            boolean r3 = r0 instanceof android.view.View
            if (r3 == 0) goto L_0x005b
            r12 = r0
            android.view.View r12 = (android.view.View) r12
            goto L_0x005c
        L_0x005b:
            r12 = 0
        L_0x005c:
            goto L_0x0000
        L_0x005d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.databinding.DataBindingUtil.findBinding(android.view.View):android.databinding.ViewDataBinding");
    }

    public static <T extends ViewDataBinding> T getBinding(View view) {
        return ViewDataBinding.getBinding(view);
    }

    public static <T extends ViewDataBinding> T setContentView(Activity activity, int layoutId) {
        return setContentView(activity, layoutId, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T setContentView(Activity activity, int layoutId, DataBindingComponent bindingComponent) {
        activity.setContentView(layoutId);
        return bindToAddedViews(bindingComponent, (ViewGroup) activity.getWindow().getDecorView().findViewById(16908290), 0, layoutId);
    }

    public static String convertBrIdToString(int id) {
        return sMapper.convertBrIdToString(id);
    }

    private static <T extends ViewDataBinding> T bindToAddedViews(DataBindingComponent component, ViewGroup parent, int startChildren, int layoutId) {
        int endChildren = parent.getChildCount();
        int childrenAdded = endChildren - startChildren;
        if (childrenAdded == 1) {
            return bind(component, parent.getChildAt(endChildren - 1), layoutId);
        }
        View[] children = new View[childrenAdded];
        for (int i = 0; i < childrenAdded; i++) {
            children[i] = parent.getChildAt(i + startChildren);
        }
        return bind(component, children, layoutId);
    }
}
