package com.example.bottomnavigationbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;


//package com.xxmassdeveloper.mpchartexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

//import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
//import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class TrendingFragment extends Fragment implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {
    private Legend legend;
    private LineChart chart;
    private TextView tv_trends;
    private EditText et_trends;
    private RequestQueue queue;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        trendsViewModel =
//                ViewModelProviders.of(this).get(TrendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trending, container, false);
//        final TextView textView = root.findViewById(R.id.text_trends);
//        trendsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        setTitle("LineChartActivity1");

        tv_trends = root.findViewById(R.id.tv_trends);
        et_trends = root.findViewById(R.id.et_trends);

//        seekBarX = root.findViewById(R.id.seekBar1);
//        seekBarX.setOnSeekBarChangeListener(this);
//
//        seekBarY = root.findViewById(R.id.seekBar2);
//        seekBarY.setMax(180);
//        seekBarY.setOnSeekBarChangeListener(this);
        chart = root.findViewById(R.id.chart1);
        queue = Volley.newRequestQueue(getContext());
        getData("Coronavirus");

        et_trends.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    getData(et_trends.getText().toString());

                }

                return false;
            }
        });



        return root;
    }


    private void initialize_graph(){
        {   // // Chart Style // //


            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
//            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//
//            // Set the marker to the chart
//            mv.setChartView(chart);
//            chart.setMarker(mv);

            // enable scaling and dragging
            //chart.setDragEnabled(true);
            //chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            //chart.setPinchZoom(true);

        }

//        XAxis xAxis;
//        {   // // X-Axis Style // //
//            xAxis = chart.getXAxis();
//
//            // vertical grid lines
//            xAxis.enableGridDashedLine(10f, 10f, 0f);
//        }

//        YAxis yAxis;
//        {   // // Y-Axis Style // //
//            yAxis = chart.getAxisLeft();
//
//            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);
//
//            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f);
//
//            // axis range
////            yAxis.setAxisMaximum(200f);
////            yAxis.setAxisMinimum(-50f);
//        }


//        {   // // Create Limit Lines // //
//            LimitLine llXAxis = new LimitLine(9f, "Index 10");
//            llXAxis.setLineWidth(4f);
//            llXAxis.enableDashedLine(10f, 10f, 0f);
//            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            llXAxis.setTextSize(10f);
//            llXAxis.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Italic.ttf"));
//
//            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//            ll1.setLineWidth(4f);
//            ll1.enableDashedLine(10f, 10f, 0f);
//            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//            ll1.setTextSize(10f);
//            ll1.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Italic.ttf"));
//
//            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//            ll2.setLineWidth(4f);
//            ll2.enableDashedLine(10f, 10f, 0f);
//            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            ll2.setTextSize(10f);
//            ll2.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Italic.ttf"));
//
//            // draw limit lines behind data instead of on top
//            yAxis.setDrawLimitLinesBehindData(true);
//            xAxis.setDrawLimitLinesBehindData(true);
//
//            // add limit lines
//            yAxis.addLimitLine(ll1);
//            yAxis.addLimitLine(ll2);
//            //xAxis.addLimitLine(llXAxis);
//        }

        // add data
//        seekBarX.setProgress(45);
//        seekBarY.setProgress(180);
        // setData(45, 180);

        // draw points over time
//        chart.animateX(1500);

        // get the legend (only possible after setting data)
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        //chart.getsetDrawAxisLine(false);
//        Legend l = chart.getLegend();
//
//        // draw legend entries as lines
//        l.setForm(Legend.LegendForm.LINE);
//        Legend l = chart.getLegend();
//        l.setFormSize(10f); // set the size of the legend forms/shapes
//        l.setForm(LegendForm.CIRCLE); // set what type of form/shape should be used
//        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//        l.setTypeface(...);
//        l.setTextSize(12f);
//        l.setTextColor(Color.BLACK);
//        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
//        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
//
//        // set custom labels and colors
//        l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "Set1", "Set2", "Set3", "Set4", "Set5" });

        // and many more...


        legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(20f);
        //    legend.setPosition(LegendPosition.BELOW_CHART_LEFT);
//        LegendEntry legendEntryA = new LegendEntry();
//        legendEntryA.label = "searching for..";
//        legendEntryA.formColor = Color.GREEN;
//        Legend legend = chart.getLegend();
//        legend.setCustom(Arrays.asList(legendEntryA));
    }

    private void setData(JSONArray response, String query) throws JSONException {
        Log.i("json","hiii");
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {

            // float val = (float) (Math.random() * range) - 30;
            float val = (float) response.getInt(i);

            values.add(new Entry(i, val));
        }
        Log.i("val",values.toString());


        initialize_graph();



        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setLabel("Trending Chart for "+query);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Trending Chart for "+query);

            set1.setDrawIcons(false);

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f);
            //set1.disableDashedLine();
            // black lines and points
            set1.setColor(0xFF6200EE);
            set1.setCircleColor(0xFF6200EE);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);


            // draw points as solid circles
            set1.setDrawCircles(true);
            set1.setDrawCircleHole(false);

            // customize legend entry
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            //HERE
            // draw selection line as dashed
            //set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
//            set1.setDrawFilled(true);
//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return chart.getAxisLeft().getAxisMinimum();
//                }
//            });

            // set color of filled area
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            } else {
//                set1.setFillColor(Color.BLACK);
//            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);

        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }



    private void getData(String keyword) {
//        RequestQueue q = Volley.newRequestQueue(getContext());
        String url = "http://reactapp4640782493.us-east-1.elasticbeanstalk.com/trends?q="+keyword;
        Log.i("url",url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("resp",response.toString());
                        //Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
//                        t.setText(response.toString());
                        try {

                            setData(response.getJSONArray("result"),response.getString("keyword"));
                            chart.invalidate();
                            Log.i("after set data","hello");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());

                    }
                });

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);


    }




}