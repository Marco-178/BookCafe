package com.ma.isw.bookcafe.model.mo;

import java.time.LocalDate;

public class Administrator {
    private int userId;
    private LocalDate dataNomina;
    private boolean deleted;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getDataNomina() {
        return dataNomina;
    }

    public void setDataNomina(LocalDate dataNomina) {
        this.dataNomina = dataNomina;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

