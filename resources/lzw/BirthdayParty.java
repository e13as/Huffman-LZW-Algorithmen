package de.hawhamburg.hamann.ad.hashing;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BirthdayParty {

    static class Birthday {
        final int day;
        final int month;

        public Birthday(int day, int month) {
            this.day = day;
            this.month = month;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Birthday birthday)) return false;
            return day == birthday.day && month == birthday.month;
        }

        @Override
        public int hashCode() {
            return Objects.hash(day, month);
        }
    }

    public static void main(String[] args) {

        Set<Birthday> party = new HashSet<>();
        Birthday birthday;

        long birthdaysBeforeCollosion = 0;
        long RUNS = 1000000;

        ThreadLocalRandom r = ThreadLocalRandom.current();

        for (int run = 0; run < RUNS; run++) {
            party = new HashSet<>();

            do {
                birthday = new Birthday(r.nextInt(30),
                        r.nextInt(12));

                birthdaysBeforeCollosion++;

            } while (party.add(birthday));
        }

        double avg = (double) birthdaysBeforeCollosion / RUNS;

        System.out.printf("Average collisions after %f2\n", avg);
    }
}
