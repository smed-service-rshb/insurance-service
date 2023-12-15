package ru.softlab.efr.services.insurance.pojo;

import org.springframework.util.CollectionUtils;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.insurance.model.db.Insurance;
import ru.softlab.efr.services.insurance.model.db.InsuranceStatus;
import ru.softlab.efr.services.insurance.model.enums.InsuranceStatusCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Описание перехода в определенный статус
 */
public class Transition {

    /**
     * Целевой статус
     */
    private InsuranceStatusCode targetStatus;

    /**
     * Права, которые необходимо иметь, для перехода в данный статус
     */
    private Set<Right> rights = new HashSet<>();

    /**
     * Права, которых не должно быть, для перехода в данный статус
     */
    private Set<Right> rightsExcluded = new HashSet<>();

    /**
     * Функция, которую надо вызвать при переходе в определенный статус
     * В случае, если функция возвращает true переход возможен, если False
     */
    private Function<Insurance, Result> before = insurance -> Result.success();

    public Transition() {
    }

    public Transition(InsuranceStatusCode targetStatus, Set<Right> rights, Function<Insurance, Result> before) {
        this.targetStatus = targetStatus;
        this.rights = rights;
        this.before = before;
    }

    public void addRight(Right right) {
        rights.add(right);
    }

    public void addRightExcluded(Right right) {
        rightsExcluded.add(right);
    }

    /**
     * Проверить, доступен ли переход для заданных прав
     * @param rights права пользователя
     * @return true - переход в статус доступен
     */
    public boolean availableForRights(List<Right> rights) {
        if (CollectionUtils.isEmpty(this.rights) && CollectionUtils.isEmpty(this.rightsExcluded)) {
            return true;
        }
        if (!CollectionUtils.isEmpty(this.rights) && (CollectionUtils.isEmpty(rights) || !rights.containsAll(this.rights))) {
            return false;
        }
        if (!CollectionUtils.isEmpty(this.rightsExcluded)) {
            if (CollectionUtils.isEmpty(rights)) {
                return true;
            }
            return Collections.disjoint(rights, this.rightsExcluded);
        }
        return true;
    }

    /**
     *
     * @param insurance
     * @return
     */
    public Result checkTransitionPossibility(Insurance insurance) {
        if (before == null) return Result.success();
        return before.apply(insurance);
    }

    public String getTargetStatusName() {
        return targetStatus != null ? targetStatus.getNameStatus() : "";
    }

    public Transition(InsuranceStatusCode targetStatus) {
        this.targetStatus = targetStatus;
    }

    public Transition(InsuranceStatusCode targetStatus, Right right) {
        this.targetStatus = targetStatus;
        addRight(right);
    }

    public InsuranceStatusCode getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(InsuranceStatusCode targetStatus) {
        this.targetStatus = targetStatus;
    }

    public Set<Right> getRights() {
        return rights;
    }

    public void setRights(Set<Right> rights) {
        this.rights = rights;
    }

    public Function<Insurance, Result> getBefore() {
        return before;
    }

    public void setBefore(Function<Insurance, Result> before) {
        this.before = before;
    }


}
