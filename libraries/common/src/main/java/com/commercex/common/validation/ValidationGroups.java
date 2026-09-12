package com.commercex.common.validation;

/**
 * Marker interfaces for Bean Validation groups.
 *
 * These groups allow different validation rules
 * for Create, Update and Delete operations.
 */
public final class ValidationGroups {

    private ValidationGroups() {
    }

    /**
     * Validation rules for Create APIs.
     */
    public interface Create {
    }

    /**
     * Validation rules for Update APIs.
     */
    public interface Update {
    }

    /**
     * Validation rules for Delete APIs.
     */
    public interface Delete {
    }
}