package com.example.demo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ReadOnlyHasValue;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import lombok.Getter;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        Binder<CampaignGeneratorSQL> campaignGeneratorSQLBinder = new Binder<>(CampaignGeneratorSQL.class);
        campaignGeneratorSQLBinder.setBean(new CampaignGeneratorSQL());

        FormLayout formLayout = new FormLayout();
        add(formLayout);
        // CAMPAIGN GENERATOR SQL
        TextField campaignName = new TextField("Campaign Name");
        campaignGeneratorSQLBinder.bind(campaignName, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getName(), (campaignGeneratorSQL, campaignNameValue) -> campaignGeneratorSQL.getCampaign().setName(campaignNameValue));
        TextField couponId = new TextField("Coupon Id");
        campaignGeneratorSQLBinder.bind(couponId, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getCouponId(), (campaignGeneratorSQL, couponIdValue) -> campaignGeneratorSQL.getCampaign().setCouponId(couponIdValue));
        NumberField layoutId = new NumberField("Layout Id");
        campaignGeneratorSQLBinder.bind(layoutId, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getLayoutId(), (campaignGeneratorSQL, layoutIdValue) -> campaignGeneratorSQL.getCampaign().setLayoutId(layoutIdValue));
        TextField percentage = new TextField("Percentage");
        campaignGeneratorSQLBinder.bind(percentage, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getPercentage(), (campaignGeneratorSQL, percentageValue) -> campaignGeneratorSQL.getCampaign().setPercentage(percentageValue));
        TextField volume = new TextField("Volume");
        campaignGeneratorSQLBinder.bind(volume, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getVolume(), (campaignGeneratorSQL, volumeValue) -> campaignGeneratorSQL.getCampaign().setVolume(volumeValue));
        TextField description = new TextField("Description");
        campaignGeneratorSQLBinder.bind(description, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getDescription(), (campaignGeneratorSQL, descriptionValue) -> campaignGeneratorSQL.getCampaign().setDescription(descriptionValue));
        TextField discountTitle = new TextField("Discount Title");
        campaignGeneratorSQLBinder.bind(discountTitle, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getDiscountTitle(), (campaignGeneratorSQL, discountTitleValue) -> campaignGeneratorSQL.getCampaign().setDiscountTitle(discountTitleValue));
        TextField recommendationCode = new TextField("Recommendation Code");
        campaignGeneratorSQLBinder.bind(recommendationCode, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().getRecommendationCode(), (campaignGeneratorSQL, recommendationCodeValue) -> campaignGeneratorSQL.getCampaign().setRecommendationCode(recommendationCodeValue));
        formLayout.add(campaignName, couponId, layoutId, percentage, volume, description, discountTitle, recommendationCode);
        Checkbox atWork = new Checkbox("At Work");
        campaignGeneratorSQLBinder.bind(atWork, campaignGeneratorSQL -> campaignGeneratorSQL.getCampaign().isAtWork(), (campaignGeneratorSQL, atWorkValue) -> campaignGeneratorSQL.getCampaign().setAtWork(atWorkValue));
        formLayout.add(atWork);

        // COUPON GENERATOR SQL
        TextField couponCode = new TextField("Coupon Code");
        campaignGeneratorSQLBinder.bind(couponCode, campaignGeneratorSQL -> campaignGeneratorSQL.getCoupon().getCouponCode(), (campaignGeneratorSQL, couponCodeValue) -> campaignGeneratorSQL.getCoupon().setCouponCode(couponCodeValue));
        NumberField used = new NumberField("Used");
        campaignGeneratorSQLBinder.bind(used, campaignGeneratorSQL -> campaignGeneratorSQL.getCoupon().getUsed(), (campaignGeneratorSQL, usedValue) -> campaignGeneratorSQL.getCoupon().setUsed(usedValue));
        formLayout.add(couponCode, used);

        //OUTPUT
        HorizontalLayout campaignSQLLayout = new HorizontalLayout();
        add(campaignSQLLayout);
        TextArea campaignSQL = new TextArea("Campaign SQL");
        campaignSQL.setReadOnly(true);
        campaignSQL.setValue(campaignGeneratorSQLBinder.getBean().toSQL());
        campaignGeneratorSQLBinder.addStatusChangeListener(event -> campaignSQL.setValue(campaignGeneratorSQLBinder.getBean().toSQL()));
        campaignSQLLayout.add(campaignSQL);
        Button button = new Button("Copy to clipboard", VaadinIcon.COPY.create());
        button.addClickListener(e -> UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", campaignSQL.getValue()));
        campaignSQLLayout.add(button);

        TextField fileName = new TextField("File Name");
        fileName.setReadOnly(true);
        campaignGeneratorSQLBinder.addStatusChangeListener(event -> fileName.setValue(campaignGeneratorSQLBinder.getBean().getFileName()));
        add(fileName);

        // Controls
        Button clearButton = new Button("Clear");
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearButton.addClickListener(event -> {
            campaignGeneratorSQLBinder.readBean(new CampaignGeneratorSQL());
            campaignSQL.setValue(campaignGeneratorSQLBinder.getBean().toSQL());
            fileName.setValue(campaignGeneratorSQLBinder.getBean().getFileName());
        });
        add(clearButton);
    }

}
