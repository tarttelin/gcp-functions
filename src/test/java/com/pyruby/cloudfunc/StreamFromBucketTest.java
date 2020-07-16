package com.pyruby.cloudfunc;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamFromBucketTest {

    @Test
    public void convertJsonFile() {
        String dob = "2011-12-03";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = Date.from(LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneId.of("UTC")).toInstant());
        assertThat(format.format(date)).isEqualTo("20111203");
    }
}
