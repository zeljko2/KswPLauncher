package android.support.constraint.solver.state;

import android.support.constraint.solver.widgets.ConstraintWidget;

public class Dimension {
    public static final Object FIXED_DIMENSION = new Object();
    public static final Object PARENT_DIMENSION = new Object();
    public static final Object PERCENT_DIMENSION = new Object();
    public static final Object SPREAD_DIMENSION = new Object();
    public static final Object WRAP_DIMENSION = new Object();
    private final int WRAP_CONTENT = -2;
    Object mInitialValue = WRAP_DIMENSION;
    boolean mIsSuggested = false;
    int mMax = Integer.MAX_VALUE;
    int mMin = 0;
    float mPercent = 1.0f;
    float mRatio = 1.0f;
    int mValue = 0;

    public enum Type {
        FIXED,
        WRAP,
        MATCH_PARENT,
        MATCH_CONSTRAINT
    }

    private Dimension() {
    }

    private Dimension(Object type) {
        this.mInitialValue = type;
    }

    public static Dimension Suggested(int value) {
        Dimension dimension = new Dimension();
        dimension.suggested(value);
        return dimension;
    }

    public static Dimension Suggested(Object startValue) {
        Dimension dimension = new Dimension();
        dimension.suggested(startValue);
        return dimension;
    }

    public static Dimension Fixed(int value) {
        Dimension dimension = new Dimension(FIXED_DIMENSION);
        dimension.fixed(value);
        return dimension;
    }

    public static Dimension Fixed(Object value) {
        Dimension dimension = new Dimension(FIXED_DIMENSION);
        dimension.fixed(value);
        return dimension;
    }

    public static Dimension Percent(Object key, float value) {
        Dimension dimension = new Dimension(PERCENT_DIMENSION);
        dimension.percent(key, value);
        return dimension;
    }

    public static Dimension Parent() {
        return new Dimension(PARENT_DIMENSION);
    }

    public static Dimension Wrap() {
        return new Dimension(WRAP_DIMENSION);
    }

    public static Dimension Spread() {
        return new Dimension(SPREAD_DIMENSION);
    }

    public Dimension percent(Object key, float value) {
        this.mPercent = value;
        return this;
    }

    public Dimension min(int value) {
        if (value >= 0) {
            this.mMin = value;
        }
        return this;
    }

    public Dimension min(Object value) {
        if (value == WRAP_DIMENSION) {
            this.mMin = -2;
        }
        return this;
    }

    public Dimension max(int value) {
        if (this.mMax >= 0) {
            this.mMax = value;
        }
        return this;
    }

    public Dimension max(Object value) {
        Object obj = WRAP_DIMENSION;
        if (value == obj && this.mIsSuggested) {
            this.mInitialValue = obj;
            this.mMax = Integer.MAX_VALUE;
        }
        return this;
    }

    public Dimension suggested(int value) {
        this.mIsSuggested = true;
        return this;
    }

    public Dimension suggested(Object value) {
        this.mInitialValue = value;
        this.mIsSuggested = true;
        return this;
    }

    public Dimension fixed(Object value) {
        this.mInitialValue = value;
        if (value instanceof Integer) {
            this.mValue = ((Integer) value).intValue();
            this.mInitialValue = null;
        }
        return this;
    }

    public Dimension fixed(int value) {
        this.mInitialValue = null;
        this.mValue = value;
        return this;
    }

    public Dimension ratio(float ratio) {
        return this;
    }

    /* access modifiers changed from: package-private */
    public void setValue(int value) {
        this.mIsSuggested = false;
        this.mInitialValue = null;
        this.mValue = value;
    }

    /* access modifiers changed from: package-private */
    public int getValue() {
        return this.mValue;
    }

    /* access modifiers changed from: package-private */
    public void setRatio(float value) {
        this.mRatio = value;
    }

    /* access modifiers changed from: package-private */
    public float getRatio() {
        return this.mRatio;
    }

    public void apply(State state, ConstraintWidget constraintWidget, int orientation) {
        if (orientation == 0) {
            if (this.mIsSuggested) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
                int type = 0;
                Object obj = this.mInitialValue;
                if (obj == WRAP_DIMENSION) {
                    type = 1;
                } else if (obj == PERCENT_DIMENSION) {
                    type = 2;
                }
                constraintWidget.setHorizontalMatchStyle(type, this.mMin, this.mMax, this.mPercent);
                return;
            }
            int i = this.mMin;
            if (i > 0) {
                constraintWidget.setMinWidth(i);
            }
            int i2 = this.mMax;
            if (i2 < Integer.MAX_VALUE) {
                constraintWidget.setMaxWidth(i2);
            }
            Object obj2 = this.mInitialValue;
            if (obj2 == WRAP_DIMENSION) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            } else if (obj2 == PARENT_DIMENSION) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
            } else if (obj2 == null) {
                constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidget.setWidth(this.mValue);
            }
        } else if (this.mIsSuggested) {
            constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT);
            int type2 = 0;
            Object obj3 = this.mInitialValue;
            if (obj3 == WRAP_DIMENSION) {
                type2 = 1;
            } else if (obj3 == PERCENT_DIMENSION) {
                type2 = 2;
            }
            constraintWidget.setVerticalMatchStyle(type2, this.mMin, this.mMax, this.mPercent);
        } else {
            int i3 = this.mMin;
            if (i3 > 0) {
                constraintWidget.setMinHeight(i3);
            }
            int i4 = this.mMax;
            if (i4 < Integer.MAX_VALUE) {
                constraintWidget.setMaxHeight(i4);
            }
            Object obj4 = this.mInitialValue;
            if (obj4 == WRAP_DIMENSION) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
            } else if (obj4 == PARENT_DIMENSION) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.MATCH_PARENT);
            } else if (obj4 == null) {
                constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
                constraintWidget.setHeight(this.mValue);
            }
        }
    }
}
