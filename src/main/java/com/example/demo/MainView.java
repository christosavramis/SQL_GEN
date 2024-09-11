package com.example.demo;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    public static String sanitizeString(String s) {
        return s.replace("[^\\p{L}\\p{N}\\p{P}\\p{Z}]", "").strip();
    }

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

        HorizontalLayout campaignSQLLayout = new HorizontalLayout();
        campaignSQLLayout.getStyle().setWidth("100%");
        campaignSQLLayout.getStyle().setPosition(Style.Position.RELATIVE);
        add(campaignSQLLayout);

        TextArea campaignSQL = new TextArea("Campaign SQL");
        campaignSQL.getStyle().setWidth("100%");
        campaignSQL.getStyle().setHeight("200px");
        campaignSQL.setReadOnly(true);
        campaignSQL.setValue(binder.getBean().toSQL());
        campaignSQLLayout.add(campaignSQL);

        Button button = new Button("", VaadinIcon.COPY.create());
        button.getStyle().setPosition(Style.Position.ABSOLUTE);
        button.getStyle().setRight("0");
        button.getStyle().setTop("0");
        button.addClickListener(e -> UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", campaignSQL.getValue()));
        campaignSQLLayout.add(button);

        TextField fileName = new TextField("File Name");
        fileName.setReadOnly(true);
        fileName.setValue(binder.getBean().getFileName());
        add(fileName);

        Button generateSQLButton = new Button("Generate SQL");
        generateSQLButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateSQLButton.addClickListener(e -> {
            if (binder.validate().isOk()) {
                campaignSQL.setValue(binder.getBean().toSQL());
                fileName.setValue(binder.getBean().getFileName());
            } else {
                Notification notification = new Notification("Παρακαλώ συμπληρώστε τα υποχρεωτικά πεδία", 3000, Notification.Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });
        add(generateSQLButton);
    }

}
