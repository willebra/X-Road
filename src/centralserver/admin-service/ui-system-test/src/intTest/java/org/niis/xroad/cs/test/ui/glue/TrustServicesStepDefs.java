/*
 * The MIT License
 *
 * Copyright (c) 2019- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2018 Estonian Information System Authority (RIA),
 * Nordic Institute for Interoperability Solutions (NIIS), Population Register Centre (VRK)
 * Copyright (c) 2015-2017 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.niis.xroad.cs.test.ui.glue;

import com.codeborne.selenide.Condition;
import io.cucumber.java.en.Step;
import org.junit.jupiter.api.Assertions;
import org.niis.xroad.cs.test.ui.constants.Constants;
import org.niis.xroad.cs.test.ui.page.TrustServicesPageObj;
import org.niis.xroad.cs.test.ui.utils.CertificateUtils;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class TrustServicesStepDefs extends BaseUiStepDefs {
    private static final String CERTIFICATE_PROFILE = "ee.ria.xroad.common.certificateprofile.impl.FiVRKCertificateProfileInfoProvider";
    private static final String NEW_CERTIFICATE_PROFILE = "ee.ria.xroad.common.certificateprofile.impl.BasicCertificateProfileInfoProvider";

    private final TrustServicesPageObj trustServicesPageObj = new TrustServicesPageObj();

    private String certificationServiceName;
    private X509Certificate testCertificate;

    @Step("new certification service is added")
    public void newCertificationServiceIsAdded() throws Exception {
        trustServicesPageObj.btnAddCertificationService().click();

        certificationServiceName = "e2e-test-cert-service-" + UUID.randomUUID();
        final byte[] certificate = CertificateUtils.generateAuthCert(certificationServiceName);

        testCertificate = CertificateUtils.readCertificate(certificate);

        trustServicesPageObj.addDialog.inputFile().uploadFile(CertificateUtils.getAsFile(certificate));
        commonPageObj.dialog.btnSave().click();
        trustServicesPageObj.addCaSettingsDialog.inputCertificateProfile().setValue(CERTIFICATE_PROFILE);
        commonPageObj.dialog.btnSave().click();

        commonPageObj.snackBar.success().shouldBe(Condition.visible);
        commonPageObj.snackBar.btnClose().click();
    }


    @Step("new certification service is visible in the Certification Services list")
    public void newCertificationServiceIsVisibleInTheList() {
        trustServicesPageObj.tableServicesRowOf(certificationServiceName).should(appear);
    }

    @Step("user is able to sort by column {int}")
    public void userIsAbleToSortByColumn(int columnIndex) {
        var column = trustServicesPageObj.tableServicesCol(columnIndex);
        Assertions.assertEquals("none", column.getAttribute("aria-sort"));
        column.click();
        Assertions.assertEquals("ascending", column.getAttribute("aria-sort"));
        column.click();
        Assertions.assertEquals("descending", column.getAttribute("aria-sort"));
    }


    @Step("user opens certification service details")
    public void userOpensCertificationServiceDetails() {
        trustServicesPageObj.tableServicesRowOf(certificationServiceName).click();
    }

    @Step("certification service details are displayed")
    public void certificationServiceDetailsAreVisible() {
        final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

        trustServicesPageObj.cardSubjectDn().shouldHave(text(testCertificate.getSubjectDN().getName()));
        trustServicesPageObj.cardIssuerDn().shouldHave(text(testCertificate.getIssuerDN().getName()));
        trustServicesPageObj.cardValidFrom().shouldHave(text(sdf.format(testCertificate.getNotBefore())));
        trustServicesPageObj.cardValidTo().shouldHave(text(sdf.format(testCertificate.getNotAfter())));
    }

    @Step("user is able to view the certificate")
    public void userIsAbleToViewTheCertificate() {
        trustServicesPageObj.certServiceDetails.btnViewCertificate().click();
        trustServicesPageObj.certificateView.certificateDetails().shouldBe(visible);
    }

    @Step("user navigates to CA settings")
    public void userNavigatesToCASettings() {
        trustServicesPageObj.certServiceDetails.tabSettings().click();
    }

    @Step("CA settings are shown")
    public void caSettingsAreShown() {
        trustServicesPageObj.certServiceDetails.caSettings.cardCertProfile().shouldHave(text(CERTIFICATE_PROFILE));
        trustServicesPageObj.certServiceDetails.caSettings.cardTlsAuth().shouldHave(text("False"));
    }

    @Step("user can change the certificate profile")
    public void userCanChangeTheCertificateProfile() {
        trustServicesPageObj.certServiceDetails.caSettings.btnEditCertProfile().click();

        clearInput(trustServicesPageObj.certServiceDetails.caSettings.inputCertProfile())
                .setValue(NEW_CERTIFICATE_PROFILE);

        commonPageObj.dialog.btnSave().shouldBe(Condition.enabled).click();

        commonPageObj.snackBar.success().shouldBe(Condition.visible);
        commonPageObj.snackBar.btnClose().click();

        trustServicesPageObj.certServiceDetails.caSettings.cardCertProfile().shouldHave(text(NEW_CERTIFICATE_PROFILE));
    }

    @Step("user can change the TLS Auth setting")
    public void userCanChangeTheTLSAuthSetting() {
        trustServicesPageObj.certServiceDetails.caSettings.btnEditTlsAuth().click();
        trustServicesPageObj.certServiceDetails.caSettings.checkboxTlsAuth().click();
        commonPageObj.dialog.btnSave().shouldBe(Condition.enabled).click();

        commonPageObj.snackBar.success().shouldBe(Condition.visible);
        commonPageObj.snackBar.btnClose().click();

        trustServicesPageObj.certServiceDetails.caSettings.cardTlsAuth().shouldHave(text("True"));
    }
}
