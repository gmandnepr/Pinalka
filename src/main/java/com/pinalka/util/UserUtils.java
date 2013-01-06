package com.pinalka.util;

import com.pinalka.domain.User;
import com.pinalka.domain.UserRole;
import com.pinalka.domain.VisibilityPolicy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;


/**
 * Provide some utility operations on user
 *
 * @author gman
 */
public final class UserUtils {

    private static final String EMPTY_STRING = "";
    private static final String ANONYMOUS_NAME = "anonymousUser";
    private static final UserRole ANONYMOUS_ROLE = UserRole.ROLE_ANONYMOUS;
    private static final Locale ANONYMOUS_LOCALE = Locale.getDefault();

    private UserUtils() {
    }

    /**
     * Creates NULL-user
     *
     * @return anonymous user
     */
    public static User createAnonymousUser() {
        final User user = new User();
        user.setId(Long.valueOf(-1L));
        user.setName(ANONYMOUS_NAME);
        user.setPass(EMPTY_STRING);
        user.setRole(ANONYMOUS_ROLE);
        user.setEmail(EMPTY_STRING);
        user.setVisibilityPolicy(VisibilityPolicy.NOTHING_VISIBLE);
        user.setLocale(ANONYMOUS_LOCALE);
        user.setRegistrationDate(new Date());
        user.setLastActionDate(new Date());
        return user;
    }

    /**
     * Replaces null values in the fields with the default values
     *
     * @param user to update fields
     */
    public static void fillNullFields(final User user) {
        if(user.getName() == null) {
            user.setName(EMPTY_STRING);
        }
        if(user.getPass() ==  null){
            user.setPass(md5(EMPTY_STRING));
        }
        if(user.getRole() == null){
            user.setRole(ANONYMOUS_ROLE);
        }
        if(user.getEmail() == null){
            user.setEmail(EMPTY_STRING);
        }
        if(user.getVisibilityPolicy() == null){
            user.setVisibilityPolicy(VisibilityPolicy.EVERY_SUBJECT_VISIBLE);
        }
        if(user.getLocale() == null){
            user.setLocale(Locale.getDefault());
        }
        if(user.getRegistrationDate() == null){
            user.setRegistrationDate(new Date());
        }
        if(user.getLastActionDate() == null){
            user.setLastActionDate(new Date());
        }
    }

    /**
     * Apply difference to the given user
     *
     * @param receiver object to apply diff
     * @param diff to apply
     * @param pass new password
     */
    public static void merge(final User receiver, final User diff, final String pass) {
        if(diff.getEmail() != null && !diff.getEmail().isEmpty()) {
            receiver.setEmail(diff.getEmail());
        }
        if(diff.getVisibilityPolicy() != null) {
            receiver.setVisibilityPolicy(diff.getVisibilityPolicy());
        }
        if(diff.getRole() != null){
            receiver.setRole(diff.getRole());            
        }
        if(pass!= null && !pass.isEmpty()){
            receiver.setPass(md5(pass));
        }
    }
    
    /**
     * Determines if user is anonymous
     *
     * @param user to check
     * @return {@code true} if user is anonymous, {@code false} otherwise
     */
    public static boolean isAnonymousUser(final User user) {
        return user.getRole() == UserRole.ROLE_ANONYMOUS;
    }

    /**
     * Check if user1 and user2 are the same user
     *
     * @param user1 first arg
     * @param user2 second arg
     * @return {@code true} if same, {@code false} otherwise
     */
    public static boolean isSame(final User user1, final User user2) {
        return user1.getId().equals(user2.getId());
    }

    /**
     * CHeck if user can access private data of the owner
     *
     * @param owner data owner
     * @param user current user
     * @return {@code true} if user can access owners data otherwise {@code false}
     */
    public static boolean isAccessible(final User owner, final User user) {
        return !UserUtils.isAnonymousUser(owner) &&
                (UserUtils.isSame(user, owner) || user.getRole() == UserRole.ROLE_ADMIN);
    }

    /**
     * Encodes password for the user
     *
     * @param user to encode password
     */
    public static void encodePassword(final User user) {
        user.setPass(md5(user.getPass()));
    }

    public static String md5(final String pass){
        final int mask = 0xFF;
        final StringBuilder res = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(pass.getBytes());
            byte[] md5 = algorithm.digest();
            for (byte b:  md5) {
                final String tmp = (Integer.toHexString(mask & b));
                if (tmp.length() == 1) {
                    res.append('0');
                }
                res.append(tmp);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return res.toString();
    }
}
