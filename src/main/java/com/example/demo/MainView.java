package com.example.demo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import lombok.Getter;

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


        add(new H3("Campaign Section"));

        FormLayout campaignFormLayout = new FormLayout();
        TextField campaignName = new TextField("Campaign Name");
        binder.forField(campaignName)
                .withValidator(name -> !name.isBlank(), "Campaign name is required")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getName(), (campaignGeneratorSQL, campaignNameValue) -> campaignGeneratorSQL.getCampaign().setName(campaignNameValue));

        IntegerField couponId = new IntegerField("Coupon Id");
        binder.forField(couponId)
                .withValidator(Objects::nonNull, "Coupon Id is required")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getCouponId(), (campaignGeneratorSQL, couponIdValue) -> campaignGeneratorSQL.getCampaign().setCouponId(couponIdValue));

        IntegerField layoutId = new IntegerField("Layout Id");
        binder.forField(layoutId)
                .withValidator(Objects::nonNull, "Layout Id is required")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getLayoutId(), (campaignGeneratorSQL, layoutIdValue) -> campaignGeneratorSQL.getCampaign().setLayoutId(layoutIdValue));

        IntegerField percentage = new IntegerField("Percentage");
        binder.forField(percentage)
                .withValidator(percentageValue -> percentageValue == null || percentageValue >= 0 && percentageValue <= 100, "Percentage must be between 0 and 100")
                .bind(campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getPercentage(), (campaignGeneratorSQL, percentageValue) -> campaignGeneratorSQL.getCampaign().setPercentage(percentageValue));

        IntegerField volume = new IntegerField("Volume");
        binder.bind(volume, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getVolume(), (campaignGeneratorSQL, volumeValue) -> campaignGeneratorSQL.getCampaign().setVolume(volumeValue));

        TextField description = new TextField("Description");
        binder.bind(description, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getDescription(), (campaignGeneratorSQL, descriptionValue) -> campaignGeneratorSQL.getCampaign().setDescription(descriptionValue));

        TextField discountTitle = new TextField("Discount Title");
        binder.bind(discountTitle, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getDiscountTitle(), (campaignGeneratorSQL, discountTitleValue) -> campaignGeneratorSQL.getCampaign().setDiscountTitle(discountTitleValue));

        TextField recommendationCode = new TextField("Recommendation Code");
        binder.bind(recommendationCode, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getRecommendationCode(), (campaignGeneratorSQL, recommendationCodeValue) -> campaignGeneratorSQL.getCampaign().setRecommendationCode(recommendationCodeValue));

        Checkbox atWork = new Checkbox("At Work");
        binder.bind(atWork, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().isAtWork(), (campaignGeneratorSQL, atWorkValue) -> campaignGeneratorSQL.getCampaign().setAtWork(atWorkValue));

        campaignFormLayout.add(campaignName, couponId, layoutId, percentage, volume, description, discountTitle, recommendationCode, atWork);
        add(campaignFormLayout);

        add(new H3("Coupon Section"));
        FormLayout couponFormLayout = new FormLayout();
        IntegerField couponCode = new IntegerField("Coupon Code");
        binder.bind(couponCode, campaignGeneratorSQL -> campaignGeneratorSQL.getCoupon().getCouponCode(), (campaignGeneratorSQL, couponCodeValue) -> campaignGeneratorSQL.getCoupon().setCouponCode(couponCodeValue));

        IntegerField used = new IntegerField("Used");
        binder.bind(used, campaignGeneratorSQL -> campaignGeneratorSQL.getCoupon().getUsed(), (campaignGeneratorSQL, usedValue) -> campaignGeneratorSQL.getCoupon().setUsed(usedValue));
        couponFormLayout.add(couponCode, used);
        add(couponFormLayout);

        add(new H3("Product Line Campaign Section"));
        FormLayout productLineCampaignFormLayout = new FormLayout();
        CheckboxGroup<ProductLineKey> productLineKeys = new CheckboxGroup<>();
        productLineKeys.setItems(ProductLineKey.values());
        productLineKeys.setItemLabelGenerator(ProductLineKey::name);
        binder.forField(productLineKeys)
                .withValidator(productLineKeysValue -> !productLineKeysValue.isEmpty(), "At least one product line key is required")
                .bind(campaignGeneratorSQL -> Set.of(campaignGeneratorSQL.getProductLineCampaign().getProductLineKeys().toArray(new ProductLineKey[0])), (campaignGeneratorSQL, productLineKeysValue) -> campaignGeneratorSQL.getProductLineCampaign().setProductLineKeys(new ArrayList<>(productLineKeysValue)));
        productLineCampaignFormLayout.add(productLineKeys);
        add(productLineCampaignFormLayout);

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
        add(fileName);

        Button generateSQLButton = new Button("Generate SQL");
        generateSQLButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateSQLButton.addClickListener(e -> {
            if (binder.validate().isOk()) {
                campaignSQL.setValue(binder.getBean().toSQL());
                fileName.setValue(binder.getBean().getFileName());
            }
        });
        add(generateSQLButton);
    }

}
