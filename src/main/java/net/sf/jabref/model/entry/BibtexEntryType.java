/*
Copyright (C) 2003 David Weitzman, Morten O. Alver

All programs in this directory and
subdirectories are published under the GNU General Public License as
described below.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or (at
your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
USA

Further information about the GNU GPL is available at:
http://www.gnu.org/copyleft/gpl.ja.html

Note:
Modified for use in JabRef.

*/
package net.sf.jabref.model.entry;

import java.util.*;
import java.util.stream.Collectors;

import net.sf.jabref.model.database.BibtexDatabase;

/**
 * Provides a list of known entry types
 * <p>
 * The list of optional and required fields is derived from http://en.wikipedia.org/wiki/BibTeX#Entry_types
 */
public abstract class BibtexEntryType implements Comparable<BibtexEntryType> {

    public abstract String getName();

    private List<String> requiredFields = new ArrayList<>();

    private List<String> optionalFields = new ArrayList<>();

    @Override
    public int compareTo(BibtexEntryType o) {
        return getName().compareTo(o.getName());
    }

    public List<String> getOptionalFields() {
        return Collections.unmodifiableList(optionalFields);
    }

    public List<String> getRequiredFields() {
        return Collections.unmodifiableList(requiredFields);
    }

    void addAllOptional(String... fieldNames) {
        optionalFields.addAll(Arrays.asList(fieldNames));
    }

    void addAllRequired(String... fieldNames) {
        requiredFields.addAll(Arrays.asList(fieldNames));
    }

    public List<String> getPrimaryOptionalFields() {
        return getOptionalFields();
    }

    public List<String> getSecondaryOptionalFields() {
        List<String> optionalFields = getOptionalFields();

        if (optionalFields == null) {
            return new ArrayList<>(0);
        }

        return optionalFields.stream().filter(field -> !isPrimary(field)).collect(Collectors.toList());
    }

    public abstract boolean hasAllRequiredFields(BibtexEntry entry, BibtexDatabase database);

    public String[] getUtilityFields() {
        return new String[]{"search"};
    }

    public boolean isRequired(String field) {
        List<String> requiredFields = getRequiredFields();
        if (requiredFields == null) {
            return false;
        }
        for (String requiredField : requiredFields) {
            if (requiredField.equals(field)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOptional(String field) {
        List<String> optionalFields = getOptionalFields();

        if (optionalFields == null) {
            return false;
        }

        return Arrays.asList(optionalFields).contains(field);
    }

    private boolean isPrimary(String field) {
        List<String> primaryFields = getPrimaryOptionalFields();

        if (primaryFields == null) {
            return false;
        }

        return Arrays.asList(primaryFields).contains(field);
    }

    public boolean isVisibleAtNewEntryDialog() {
        return true;
    }


    /**
     * Get an array of the required fields in a form appropriate for the entry customization
     * dialog - that is, the either-or fields together and separated by slashes.
     *
     * @return Array of the required fields in a form appropriate for the entry customization dialog.
     */
    public List<String> getRequiredFieldsForCustomization() {
        return getRequiredFields();
    }
}