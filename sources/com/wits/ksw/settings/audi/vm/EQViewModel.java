package com.wits.ksw.settings.audi.vm;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.database.ContentObserver;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import com.wits.ksw.settings.utlis_view.FileUtils;
import com.wits.ksw.settings.utlis_view.KeyConfig;
import com.wits.pms.statuscontrol.PowerManagerApp;

public class EQViewModel extends AndroidViewModel {
    /* access modifiers changed from: private */
    public static final String TAG = ("KSWLauncher." + EQViewModel.class.getSimpleName());
    public ObservableInt bassProgress = new ObservableInt();
    public ObservableField<String> bassStr = new ObservableField<>();
    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange) {
            try {
                int eq = PowerManagerApp.getSettingsInt(KeyConfig.EQ_MODE);
                Log.i(EQViewModel.TAG, "onChange: " + eq);
                if (eq == 0) {
                    EQViewModel.this.setProgress(eq, PowerManagerApp.getSettingsInt(KeyConfig.EQ_BASS), PowerManagerApp.getSettingsInt(KeyConfig.EQ_MIDDLE), PowerManagerApp.getSettingsInt(KeyConfig.EQ_TREBLE));
                } else {
                    EQViewModel.this.setProgress(eq, 12, 12, 12);
                }
                EQViewModel.this.eqModel.set(eq);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Context context;
    public ObservableInt eqModel = new ObservableInt();
    public ObservableInt mezzoProgress = new ObservableInt();
    public ObservableField<String> mezzoStr = new ObservableField<>();
    public ObservableInt trebleProgress = new ObservableInt();
    public ObservableField<String> trebleStr = new ObservableField<>();

    public EQViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        try {
            int bassValue = PowerManagerApp.getSettingsInt(KeyConfig.EQ_BASS);
            int mezzoValue = PowerManagerApp.getSettingsInt(KeyConfig.EQ_MIDDLE);
            int trebleValue = PowerManagerApp.getSettingsInt(KeyConfig.EQ_TREBLE);
            int eq = PowerManagerApp.getSettingsInt(KeyConfig.EQ_MODE);
            Log.d(TAG, "\teqModel:" + eq + "\tbassValue:" + bassValue + "\tmezzoValue:" + mezzoValue + "\ttrebleValue:" + trebleValue);
            this.eqModel.set(eq);
            if (eq == 0) {
                setProgress(eq, bassValue, mezzoValue, trebleValue);
            } else {
                setProgress(eq, 12, 12, 12);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        this.context.getContentResolver().registerContentObserver(Settings.System.getUriFor(KeyConfig.EQ_MODE), true, this.contentObserver);
    }

    /* access modifiers changed from: private */
    public void setProgress(int model, int bassValue, int mezzoValue, int trebleValue) {
        Log.i(TAG, "setProgress: \tbassValue:" + bassValue + "\tmezzoValue:" + mezzoValue + "\ttrebleValue:" + trebleValue);
        switch (model) {
            case 0:
                this.bassProgress.set(bassValue);
                this.mezzoProgress.set(mezzoValue);
                this.trebleProgress.set(trebleValue);
                break;
            case 1:
                this.bassProgress.set(bassValue + 4);
                this.mezzoProgress.set(mezzoValue - 3);
                this.trebleProgress.set(trebleValue + 4);
                break;
            case 2:
                this.bassProgress.set(bassValue + 7);
                this.mezzoProgress.set(mezzoValue + 1);
                this.trebleProgress.set(trebleValue + 3);
                break;
            case 3:
                this.bassProgress.set(bassValue + 3);
                this.mezzoProgress.set(mezzoValue - 1);
                this.trebleProgress.set(trebleValue + 5);
                break;
            case 4:
                this.bassProgress.set(bassValue + 1);
                this.mezzoProgress.set(mezzoValue + 4);
                this.trebleProgress.set(trebleValue + 4);
                break;
            case 5:
                this.bassProgress.set(bassValue + 5);
                this.mezzoProgress.set(mezzoValue - 1);
                this.trebleProgress.set(trebleValue + 7);
                break;
        }
        this.bassStr.set(String.valueOf(this.bassProgress.get() - 12));
        this.mezzoStr.set(String.valueOf(this.mezzoProgress.get() - 12));
        this.trebleStr.set(String.valueOf(this.trebleProgress.get() - 12));
    }

    public static void setEQModelChangeListener(RadioButton radioButton, final int index) {
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FileUtils.savaIntData(KeyConfig.EQ_MODE, index);
                }
            }
        });
    }

    public void setEQModelChange(RadioButton radioButton, final SeekBar seekBar, final int index) {
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FileUtils.savaIntData(KeyConfig.EQ_MODE, index);
                    seekBar.setEnabled(index == 0);
                }
            }
        });
    }

    public static void setBassSeekBarChangeListener(SeekBar seekbar, EQViewModel viewModel) {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    FileUtils.savaIntData(KeyConfig.EQ_BASS, progress);
                    EQViewModel eQViewModel = EQViewModel.this;
                    if (eQViewModel != null) {
                        eQViewModel.bassStr.set(String.valueOf(progress - 12));
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public static void setmezzoSeekBarChangeListener(SeekBar seekbar, EQViewModel viewModel) {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    FileUtils.savaIntData(KeyConfig.EQ_MIDDLE, progress);
                    EQViewModel eQViewModel = EQViewModel.this;
                    if (eQViewModel != null) {
                        eQViewModel.mezzoStr.set(String.valueOf(progress - 12));
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public static void setTrebleSeekBarChangeListener(SeekBar seekbar, EQViewModel viewModel) {
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    FileUtils.savaIntData(KeyConfig.EQ_TREBLE, progress);
                    EQViewModel eQViewModel = EQViewModel.this;
                    if (eQViewModel != null) {
                        eQViewModel.trebleStr.set(String.valueOf(progress - 12));
                    }
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onCleared() {
        this.context.getContentResolver().unregisterContentObserver(this.contentObserver);
        super.onCleared();
    }
}
