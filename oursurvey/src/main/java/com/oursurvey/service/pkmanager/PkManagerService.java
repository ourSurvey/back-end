package com.oursurvey.service.pkmanager;

import com.oursurvey.entity.*;
import com.oursurvey.exception.PkMangagerTableException;
import com.oursurvey.repo.pkmanager.PkManagerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class PkManagerService {
    private final PkManagerRepo repo;

    @Transactional(rollbackFor = Exception.class)
    public String createOrUpdateByTableName(String name) {
        String prefix = getPrefix(name);
        if (prefix == null) {
            throw new PkMangagerTableException("no table");
        }

        Optional<PkManager> opt = repo.getByTableName(name);
        LocalDate now = LocalDate.now();

        int yy = now.getYear() - 2000;
        int mm = now.getMonthValue();
        int dd = now.getDayOfMonth();
        int nowSum = yy + mm + dd;
        if (opt.isEmpty()) {
            repo.save(PkManager.builder()
                    .tableName(name)
                    .prefix(prefix)
                    .yy(yy)
                    .mm(mm)
                    .dd(dd)
                    .alpha('A')
                    .count(1)
                    .build());
        } else {
            PkManager d = opt.get();
            int sum = d.getYy() + d.getMm() + d.getDd();
            if (sum != nowSum) {
                d.update(yy, mm, dd, 'A', 1);
            } else {
                if (d.getCount().equals(999)) {
                    d.update((char) (d.getAlpha() + 1), 1);
                } else {
                    d.update(d.getCount() + 1);
                }
            }
        }

        PkManager n = repo.getByTableName(name).get();
        return n.getPrefix()
                + n.getYy()
                + ((n.getMm() / 10) > 0 ? (n.getMm()) : ("0" + n.getMm()))
                + n.getDd()
                + n.getAlpha()
                + String.format("%03d", n.getCount());
    }

    private String getPrefix(String tableName) {
        return switch (tableName) {
            case Survey.NAME -> "SUVY";
            case Section.NAME -> "SCTN";
            case Question.NAME -> "QSTN";
            case QuestionItem.NAME -> "QSTI";
            default -> null;
        };
    }
}
