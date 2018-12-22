package org.nb.bbbook.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.nb.bbbook.model.Court;
import org.nb.bbbook.view.ResultsView;
import org.nb.bbbook.view.DailySummaryView;
import org.nb.bbbook.model.Meta;
import org.nb.bbbook.view.MatchupAggView;
import org.nb.bbbook.view.MatchupView;
import org.nb.bbbook.view.SeasonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/api/v1",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class MainController {

    private final MainService mainService;
    private final ObjectMapper mapper;

    public MainController(MainService mainService, ObjectMapper mapper) {
        this.mainService = mainService;
        this.mapper = mapper;
    }

    @RequestMapping(value = {"/schedule", "/schedule/{date}"}, method = RequestMethod.GET)
    public ResponseEntity<DailySummaryView> schedule(
        @PathVariable(required = false, name = "date", value = "date")
        Optional<String> date
    ) {
        if (!date.isPresent()) {
            final DailySummaryView result = mainService.scheduleView();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            final DailySummaryView result =
                mainService.scheduleView(LocalDate.parse(date.get(), DateTimeFormatter.BASIC_ISO_DATE));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/results/{team}", method = RequestMethod.GET)
    public ResponseEntity<ResultsView> results(
        @PathVariable("team")
        String team,
        @RequestParam(required = false, name = "c", value = "c", defaultValue = "ALL")
        String c,
        @RequestParam(required = false, name = "g", value = "g")
        String g
    ) {
        int numGames = 21;
        if (g != null) {
            try {
                numGames = Integer.parseInt(g);
            } catch (NumberFormatException e)
            {
                // ignore;
            }
        }

        Court court = Court.ALL;
        if (c != null) {
            court = Court.valueOf(c.toUpperCase());
        }

        if (team.matches("([a-zA-Z]{3})")) {
            final ResultsView view = mainService.getResultsView(team, court, numGames);
            return new ResponseEntity<>(view, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/matchup/{teams}", method = RequestMethod.GET)
    public ResponseEntity<MatchupAggView> matchup(
        @PathVariable("teams")
        String teams
    ) {
        final ImmutableList<Integer> sampleDays = ImmutableList.of(3, 5, 8, 13, 21);

        if (teams.matches("([a-zA-Z]{3},[a-zA-Z]{3})")) {
            final String[] tokens = teams.split(",");
            final String visitor = tokens[0].toUpperCase();
            final String home = tokens[1].toUpperCase();
            final List<MatchupView> matchups = new ArrayList<>();
            for (int days: sampleDays) {
                mainService.createMatchupView(
                    visitor,
                    home,
                    days).ifPresent(m -> matchups.add(m));
            }

            final MatchupAggView view = new MatchupAggView(
                mainService.daysOffSinceLastGame(visitor),
                mainService.daysOffSinceLastGame(home),
                matchups
            );

            return new ResponseEntity<>(view, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/season", method = RequestMethod.GET)
    public ResponseEntity<SeasonView> season() {
        final SeasonView result = mainService.seasonView();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/refresh/{arg}", method = RequestMethod.POST)
    public ResponseEntity<Meta> refresh(
        @PathVariable("arg")
        String arg,
        @RequestParam(required = false, name = "f", value = "f", defaultValue = "false")
        boolean force
    ) {
        if (arg.equalsIgnoreCase("season")) {
            final Meta result = mainService.refreshSeason(Boolean.valueOf(force));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (arg.equalsIgnoreCase("box")) {
            final Meta result = mainService.refreshBoxScores(Boolean.valueOf(force));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/meta", method = RequestMethod.GET)
    public ResponseEntity<Meta> meta() {
        final Meta result = mainService.getMeta();
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
