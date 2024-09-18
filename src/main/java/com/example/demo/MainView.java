package com.example.demo;

import com.example.demo.data.ProductLineKey;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        Binder<CampaignGeneratorSQL> binder = new Binder<>(CampaignGeneratorSQL.class);
        binder.setBean(CampaignGeneratorSQL.sample());
        Image logo = new Image("./images/logo.svg", "Vaadin Logo");
        logo.getStyle().setWidth("300px");
        add(logo);

        FormLayout campaignFormLayout = new FormLayout();
        TextField campaignName = new TextField("Ονομασία Καμπάνιας");
        Checkbox toggleAtWork = new Checkbox("at work");
        binder.bind(toggleAtWork, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().isAtWork(), (campaignGeneratorSQL, atWorkValue) -> campaignGeneratorSQL.getCampaign().setAtWork(atWorkValue));
        campaignName.setHelperComponent(toggleAtWork);
        binder.forField(campaignName)
                .withValidator(name -> !name.isBlank(), "Η ονομασία καμπάνιας είναι υποχρεωτική")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getName(), (campaignGeneratorSQL, campaignNameValue) -> campaignGeneratorSQL.getCampaign().setName(campaignNameValue));

        IntegerField couponId = new IntegerField("Κωδικός Κουπονιού");
        couponId.setPlaceholder("π.χ. 2232");
        couponId.setHelperText("Τελευταίος κωδικός κουπονιού που χρησιμοποιήθηκε στη παραγωγική βαση + 1");
        binder.forField(couponId)
                .withValidator(Objects::nonNull, "Ο κωδικός κουπονιού είναι υποχρεωτικός")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getCouponId(), (campaignGeneratorSQL, couponIdValue) -> campaignGeneratorSQL.getCampaign().setCouponId(couponIdValue));

        IntegerField layoutId = new IntegerField("Layout Id");
        layoutId.setPlaceholder("π.χ. 1");
        layoutId.setHelperText("Ο κωδικός του layout συνήθως είναι 1");
        layoutId.setVisible(false);
        binder.forField(layoutId)
                .withValidator(Objects::nonNull, "το Layout είναι υποχρεωτικό")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getLayoutId(), (campaignGeneratorSQL, layoutIdValue) -> campaignGeneratorSQL.getCampaign().setLayoutId(layoutIdValue));

        IntegerField percentage = new IntegerField("Έκπτωση %");
        binder.forField(percentage)
                .withValidator(percentageValue -> percentageValue == null || percentageValue >= 0 && percentageValue <= 100, "Percentage must be between 0 and 100")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getPercentage(), (campaignGeneratorSQL, percentageValue) -> campaignGeneratorSQL.getCampaign().setPercentage(percentageValue));

        IntegerField volume = new IntegerField("Ποσότητα");
        volume.setHelperText("-1 για απεριόριστη ποσότητα");
        volume.setPlaceholder("π.χ. -1");
        volume.setVisible(false);
        binder.forField(volume)
                .withValidator(volumeValue -> volumeValue == null || volumeValue >= -1, "Η ποσότητα πρέπει να είναι μη αρνητικός αριθμός ή -1 για απεριόριστη ποσότητα")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getVolume(), (campaignGeneratorSQL, volumeValue) -> campaignGeneratorSQL.getCampaign().setVolume(volumeValue));

        TextField description = new TextField("Ονομασία Εταιρίας Προσφοράς (CRM)");
        description.setPlaceholder("π.χ. OHB-HELLAS (ΠΡΟΣΩΠΙΚΟ) - Έκπτωση 5%");
        binder.forField(description)
                .withValidator(descriptionValue -> !descriptionValue.isBlank(), "Η ονομασία εταιρίας προσφοράς είναι υποχρεωτική")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getDescription(), (campaignGeneratorSQL, descriptionValue) -> campaignGeneratorSQL.getCampaign().setDescription(descriptionValue));

        TextField discountTitle = new TextField("Ονομασία Εταιρίας (για το site)");
        discountTitle.setPlaceholder("π.χ. OHB-HELLAS");
        binder.forField(discountTitle)
                .withValidator(discountTitleValue -> !discountTitleValue.isBlank(), "Η ονομασία εταιρίας (για το site) είναι υποχρεωτική")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getDiscountTitle(), (campaignGeneratorSQL, discountTitleValue) -> campaignGeneratorSQL.getCampaign().setDiscountTitle(discountTitleValue));

        TextField recommendationCode = new TextField("Κωδικός σύστασης");
        binder.bind(recommendationCode, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getRecommendationCode(), (campaignGeneratorSQL, recommendationCodeValue) -> campaignGeneratorSQL.getCampaign().setRecommendationCode(recommendationCodeValue));

        IntegerField used = new IntegerField("Χρήσεις");
        used.setHelperText("Οι μέγιστες χρήσεις του κουπονιού, 0 για απεριόριστες χρήσεις");
        used.setPlaceholder("π.χ. 0");
        used.setVisible(false);
        binder.forField(used)
                .withValidator(usedValue -> usedValue == null || usedValue >= 0, "Οι χρήσεις πρέπει να είναι μη αρνητικός αριθμός")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCoupon().getUsed(), (campaignGeneratorSQL, usedValue) -> campaignGeneratorSQL.getCoupon().setUsed(usedValue));

        CheckboxGroup<ProductLineKey> productLineKeys = new CheckboxGroup<>("Product Line");
        productLineKeys.setItems(ProductLineKey.values());
        productLineKeys.setItemLabelGenerator(ProductLineKey::getStrategyDisplayKey);
        binder.forField(productLineKeys)
                .withValidator(productLineKeysValue -> !productLineKeysValue.isEmpty(), "Πρέπει να επιλέξετε τουλάχιστον ένα product line")
                .bind(campaignGeneratorSQL -> Set.of(campaignGeneratorSQL.getProductLineCampaign().getProductLineKeys().toArray(new ProductLineKey[0])), (campaignGeneratorSQL, productLineKeysValue) -> campaignGeneratorSQL.getProductLineCampaign().setProductLineKeys(new ArrayList<>(productLineKeysValue)));

        campaignFormLayout.add(
                campaignName,
                discountTitle,
                description,
                recommendationCode,
                percentage,
                couponId,
                layoutId,
                volume,
                used,
                productLineKeys);
        add(campaignFormLayout);
        TextArea guideLinesTextArea = getGuideLines();
        guideLinesTextArea.getStyle().set("width", "100%");
        guideLinesTextArea.getStyle().set("height", "200px");
        add(guideLinesTextArea);

        add(new SqlFileDownloaderView(() -> binder.getBean().toSQL(), () -> binder.getBean().getFileName(), () -> binder.validate().isOk()));
    }

    private static TextArea getGuideLines () {
        String guideLines = """
                1. Κατεβάστε το αρχείο πατώντας το κουμπί "Download SQL"
                2. Ανοίξτε το φάκελο \\\\storage-srv1\\Applications_Development\\e-services\\projects\\ANY\\campaign-requests και δημιουργήστε ενα νεο φάκελο με την τωρινή ημερομηνία πχ 2024.09.17
                3. Αντιγράψτε το αρχείο sql μέσα στον φάκελο που δημιουργήσατε και βαλτέ το παρακάτω αίτημα:
                Είδος: Database Management Requests
                Παρακαλώ να τρέξουν τα SQL Scripts που βρίσκονται στον κατάλογο \\storage-srv1\\Applications_Development\\e-services\\projects\\ANY\\campaign-requests\\2024.09.17 στην βάση παραγωγής του @nytime, ANYTIMEP, που τρέχει στο μηχάνημα με IP: 10.2.4.132 και όνομα dbs-pds1. Τα script αφορούν στην προσθήκη προωθητικών ενεργειών στην πλατφόρμα του anytime Ελλάδος.
                """;
        TextArea guideLinesTextArea = new TextArea("Οδηγίες για την προσθήκη καμπάνιας στην παραγωγική βάση");
        guideLinesTextArea.setReadOnly(true);
        guideLinesTextArea.setValue(guideLines);
        return guideLinesTextArea;
    }

    public static class SqlFileDownloaderView extends VerticalLayout {
        private final Anchor downloadAnchor = new Anchor("", "Download SQL");
        public SqlFileDownloaderView(Supplier<String> sqlContentSupplier, Supplier<String> sqlFileNameSupplier, BooleanSupplier isOkaySupplier) {
            downloadAnchor.getStyle().set("background", "var(--lumo-primary-color)");
            downloadAnchor.getStyle().set("color", "var(--lumo-base-color)");
            downloadAnchor.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
            downloadAnchor.getStyle().set("padding", "var(--lumo-space-s)");
            downloadAnchor.getStyle().set("font-size", "var(--lumo-font-size-s)");
            downloadAnchor.addAttachListener(event -> {
                if (Boolean.TRUE.equals(isOkaySupplier.getAsBoolean())) {
                    StreamResource resource = new StreamResource(sqlFileNameSupplier.get(), () -> {
                        try {
                            return new ByteArrayInputStream(sqlContentSupplier.get().getBytes(StandardCharsets.UTF_8));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
                    downloadAnchor.setHref(resource);
                    downloadAnchor.getElement().setAttribute("download", true);
                } else {
                    Notification notification = new Notification("Παρακαλώ συμπληρώστε τα υποχρεωτικά πεδία", 3000, Notification.Position.MIDDLE);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
            });
            add(downloadAnchor);
        }
    }

}
