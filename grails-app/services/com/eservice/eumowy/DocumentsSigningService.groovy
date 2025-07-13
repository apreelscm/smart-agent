package com.eservice.eumowy

import com.eservice.eumowy.documents.DocumentSigningCode
import com.eservice.eumowy.sms.SmsClient
import org.apache.commons.lang.StringUtils

import static com.eservice.eumowy.Subscription.PersonRole.ACCEPTANT1
import static com.eservice.eumowy.Subscription.PersonRole.ACCEPTANT2
import static com.eservice.eumowy.Subscription.PersonRole.ACCEPTANT3
import static com.eservice.eumowy.Subscription.PersonRole.ACCEPTANT4
import static com.eservice.eumowy.Subscription.PersonRole.PH
import static org.apache.commons.lang.StringUtils.isNotBlank

class SignDocumentCommand {
    long processId
    Subscription.PersonRole personRole
    String code
}

class ResetSigningCodeCommand {
    long processId
    Subscription.PersonRole personRole
}

class SignDocumentsResult {
    private final boolean error;

    private SignDocumentsResult(boolean error) {
        this.error = error
    }

    static SignDocumentsResult error() {
        return new SignDocumentsResult(true);
    }

    static SignDocumentsResult success() {
        return new SignDocumentsResult(false);
    }

    boolean isError() {
        return error;
    }
}
class RefreshSigningCodeResult {
    private final boolean error;

    private RefreshSigningCodeResult(boolean error) {
        this.error = error
    }

    static RefreshSigningCodeResult error() {
        return new RefreshSigningCodeResult(true);
    }

    static RefreshSigningCodeResult success() {
        return new RefreshSigningCodeResult(false);
    }

    boolean isError() {
        return error;
    }
}

class Signatory {
    String docsSigningCode
    String firstName
    String lastName
    String mobilePhoneNumber;
}

class DocumentsSigningService {
    public static final String CODE_SIGNATURE_MARKER = "SIGNED_WITH_SMS"
    public static final int PHONE_NUMBER_IDX = 1
    public static final int DATE_IDX = 2
    public static final int CODE_IDX = 3

    private static final int PH_IDX = -1
    private static final Map<Subscription.PersonRole, Integer> ROLE_TO_REPRESENTATIVE_MAPPING = new HashMap<>();
    static {
        ROLE_TO_REPRESENTATIVE_MAPPING.put(ACCEPTANT1, 0)
        ROLE_TO_REPRESENTATIVE_MAPPING.put(ACCEPTANT2, 1)
        ROLE_TO_REPRESENTATIVE_MAPPING.put(ACCEPTANT3, 2)
        ROLE_TO_REPRESENTATIVE_MAPPING.put(ACCEPTANT4, 3)
        ROLE_TO_REPRESENTATIVE_MAPPING.put(PH, PH_IDX)
    }

    SmsClient smsClient

    void generateSigningCodes(Process p) {
        log.info "Generating document signing codes"
        p.representatives.findAll { it.hasSignedContract == true }.each {
            it.documentsSigningCode = DocumentSigningCode.generate()
        }
        p.phDocsSigningCode = DocumentSigningCode.generate()
        p.save(flush: true)

        log.info "Sending document signing codes"
        p.representatives.findAll {StringUtils.isNotBlank(it.documentsSigningCode) }.each {
            sendSms(it.mobilePhone, it.documentsSigningCode)
        }
        sendSms(p.phMobilePhone, p.phDocsSigningCode)
    }

    SignDocumentsResult signDocuments(SignDocumentCommand cmd) {
        Process p = Process.findById(cmd.processId)
        if (p == null) {
            return SignDocumentsResult.error()
        }

        Signatory signatory = findSignatoryByRole(p, cmd.personRole)

        if(!validateSigningCode(signatory.docsSigningCode, cmd.code)) {
            return SignDocumentsResult.error()
        }

        Subscription s = Subscription.findByPersonRoleAndProcess(cmd.personRole, p)

        if (s == null) {
            Date signDate = new Date()
            s = new Subscription([
                    content: "${CODE_SIGNATURE_MARKER}|${signatory.mobilePhoneNumber}|${formatted(signDate)}|${signatory.docsSigningCode}",
                    name: signatory.firstName,
                    surname: signatory.lastName,
                    personRole: cmd.personRole,
                    signDate: signDate,
                    uniqueKey: cmd.processId + cmd.personRole.name(),
                    signingCode: signatory.docsSigningCode,
                    process: p,
            ])
            log.info "Saving subscription for role " + s.personRole.toString()
        } else {
            s.name = signatory.firstName
            s.surname = signatory.lastName
            s.signingCode = signatory.docsSigningCode
            s.signDate = new Date()
            log.info "Updating subscription for role " + s.personRole.toString()
        }

        s.save(flush: true)

        if (s.id != null) {
            log.info "Subscription with id ${s.id} saved"
        } else {
            log.error "Error during saving subscription with id ${s.id}"
        }

        return SignDocumentsResult.success()
    }

    RefreshSigningCodeResult refreshSigningCode(ResetSigningCodeCommand cmd) {
        Process p = Process.findById(cmd.processId)
        if (p == null) {
            log.info("Wrong process id")
            return RefreshSigningCodeResult.error()
        }

        if (!isRepresentativeRole(cmd.personRole) && !isPHRole(cmd.personRole)) {
            log.info("Unsupported person role for document signing")
            return RefreshSigningCodeResult.error()
        }

        String newCode = DocumentSigningCode.generate()
        String mobilePhoneNumber

        if (isRepresentativeRole(cmd.personRole)) {
            Representative r = findRepresentative(p, cmd.personRole)
            r.documentsSigningCode = newCode
            r.save(flush: true)
            mobilePhoneNumber = r.mobilePhone
        } else if (isPHRole(cmd.personRole)) {
            p.phDocsSigningCode = newCode
            p.save(flush: true)
            mobilePhoneNumber = p.phMobilePhone
        } else {
            // Should never happen because we validate it before
            throw new IllegalStateException()
        }

        if (mobilePhoneNumber == null || mobilePhoneNumber.trim().length() == 0) {
            log.info("Mobile phone is missing for person in role ${cmd.personRole}")
            return RefreshSigningCodeResult.error()
        }

        log.info("Rest processId: ${cmd.processId}, personRole: ${cmd.personRole}")
        sendSms(mobilePhoneNumber, newCode)

        return RefreshSigningCodeResult.success()
    }

    private void sendSms(String mobilePhone, String documentSigningCode) {
        smsClient.sendMessage(MobilePhoneNumber.of(mobilePhone), "Twój kod sms do podpisania umowy to: ${documentSigningCode}")
    }


    private static Signatory findSignatoryByRole(Process p, Subscription.PersonRole role) {
        String code
        String firstName
        String lastName
        String mobilePhoneNumber
        if (isRepresentativeRole(role)) {
            Representative rep = findRepresentative(p, role)
            code = rep.documentsSigningCode
            firstName = rep.name
            lastName = rep.surname
            mobilePhoneNumber = rep.mobilePhone
        } else if (isPHRole(role)) {
            code = p.phDocsSigningCode
            firstName = p.phFirstName
            lastName = p.phSurname
            mobilePhoneNumber = p.phMobilePhone
        } else {
            // Should never happen because we validate the role
            throw new IllegalArgumentException("Unsupported person role for document signing")
        }
        return new Signatory([
                docsSigningCode: code,
                firstName: firstName,
                lastName: lastName,
                mobilePhoneNumber: mobilePhoneNumber,
        ])
    }

    private static boolean isRepresentativeRole(Subscription.PersonRole role) {
        Integer repIdx = ROLE_TO_REPRESENTATIVE_MAPPING[role]
        return repIdx != null && repIdx != PH_IDX
    }

    private static boolean isPHRole(Subscription.PersonRole role) {
        Integer repIdx = ROLE_TO_REPRESENTATIVE_MAPPING[role]
        return repIdx == PH_IDX;
    }

    private static Representative findRepresentative(Process p, Subscription.PersonRole role) {
        int index = ROLE_TO_REPRESENTATIVE_MAPPING[role]
        Collection<Representative> reps = p.representatives.findAll { r -> r.type == Representative.Type.REPRESENTATIVE }
        if (reps.size() < index) {
            throw new IllegalStateException("Representative not found!")
        }
        return reps[index]
    }

    private static boolean validateSigningCode(String codeA, String codeB) {
        return isNotBlank(codeA) && codeA == codeB
    }

    private static String formatted(Date date) {
        return date.format("yyyy-MM-dd HH:mm:ss")
    }

}
