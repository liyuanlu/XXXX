package com.mlly.xxalarm.weather;

import java.util.List;

/**
 * Created by liyuanlu on 2018/11/16.
 */
public class GridWeatherInfo {

    private List<ResultsBean> results;

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * location : {"longitude":"116.359805","latitude":"39.865927"}
         * now_grid : {"temperature":"4.85","humidity":"31.87","wind_speed":"6.20","wind_scale":"2","wind_direction_degree":"224.24","wind_direction":"西南","precip":"0.00","pressure":"1024.16","solar_radiation":"0.00"}
         * last_update : 2018-11-16T20:00:00+08:00
         */

        private LocationBean location;
        private NowGridBean now_grid;
        private String last_update;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public NowGridBean getNow_grid() {
            return now_grid;
        }

        public void setNow_grid(NowGridBean now_grid) {
            this.now_grid = now_grid;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public static class LocationBean {
            /**
             * longitude : 116.359805
             * latitude : 39.865927
             */

            private String longitude;
            private String latitude;

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }
        }

        public static class NowGridBean {
            /**
             * temperature : 4.85
             * humidity : 31.87
             * wind_speed : 6.20
             * wind_scale : 2
             * wind_direction_degree : 224.24
             * wind_direction : 西南
             * precip : 0.00
             * pressure : 1024.16
             * solar_radiation : 0.00
             */

            private String temperature;
            private String humidity;
            private String wind_speed;
            private String wind_scale;
            private String wind_direction_degree;
            private String wind_direction;
            private String precip;
            private String pressure;
            private String solar_radiation;

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getHumidity() {
                return humidity;
            }

            public void setHumidity(String humidity) {
                this.humidity = humidity;
            }

            public String getWind_speed() {
                return wind_speed;
            }

            public void setWind_speed(String wind_speed) {
                this.wind_speed = wind_speed;
            }

            public String getWind_scale() {
                return wind_scale;
            }

            public void setWind_scale(String wind_scale) {
                this.wind_scale = wind_scale;
            }

            public String getWind_direction_degree() {
                return wind_direction_degree;
            }

            public void setWind_direction_degree(String wind_direction_degree) {
                this.wind_direction_degree = wind_direction_degree;
            }

            public String getWind_direction() {
                return wind_direction;
            }

            public void setWind_direction(String wind_direction) {
                this.wind_direction = wind_direction;
            }

            public String getPrecip() {
                return precip;
            }

            public void setPrecip(String precip) {
                this.precip = precip;
            }

            public String getPressure() {
                return pressure;
            }

            public void setPressure(String pressure) {
                this.pressure = pressure;
            }

            public String getSolar_radiation() {
                return solar_radiation;
            }

            public void setSolar_radiation(String solar_radiation) {
                this.solar_radiation = solar_radiation;
            }
        }
    }
}
