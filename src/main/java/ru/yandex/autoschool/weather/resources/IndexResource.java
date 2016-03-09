package ru.yandex.autoschool.weather.resources;

import ru.yandex.autoschool.weather.models.Weather;
import ru.yandex.autoschool.weather.services.WeatherService;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.length;

/**
 * eroshenkoam
 * 29/10/14
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class IndexResource {

    @Inject
    private WeatherService weather;

    @GET
    @Path("/weather")
    public Weather getIndex(@DefaultValue(WeatherService.DEFAULT_CITY)
                            @QueryParam("city") String city,
                            @DefaultValue(WeatherService.DEFAULT_REGION)
                            @QueryParam("region") String region) {
        return weather.getWeather(city, region);
    }

    @GET
    @Path("/suggest")
    public String suggest(@QueryParam("query") String query) {
        if (isBlank(query) || length(query) < 2) {
            return Collections.emptyList().toString();
        }

        String path = getClass().getClassLoader().getResource("data/suggest.json").getFile();

        try (Stream<String> lines = lines(Paths.get(path))) {
            return lines.filter(line -> contains(line, query))
                    .limit(5)
                    .collect(toList()).toString();
        } catch (IOException e) {
            throw new RuntimeException("Cant read suggests file", e);
        }
    }
}