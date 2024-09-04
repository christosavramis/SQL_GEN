package com.example.demo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        Binder<CampaignGeneratorSQL> campaignGeneratorSQLBinder = new Binder<>(CampaignGeneratorSQL.class);
        campaignGeneratorSQLBinder.setBean(new CampaignGeneratorSQL());

        Button clearButton = new Button("Clear");
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearButton.addClickListener(event -> campaignGeneratorSQLBinder.getBean().clear());
        add(clearButton);


        TextField text = new TextField("Text");
        campaignGeneratorSQLBinder.bind(text, CampaignGeneratorSQL::getText, CampaignGeneratorSQL::setText);
        add(text);

        CampaignEditor campaignEditor = new CampaignEditor();
        campaignEditor.addValueChangeListener(event -> System.out.println(event.getValue() + "inside"));
        campaignGeneratorSQLBinder.bind(campaignEditor, CampaignGeneratorSQL::getCampaign, CampaignGeneratorSQL::setCampaign);
        add(campaignEditor);

        TextArea campaignSQL = new TextArea("Campaign SQL");
        campaignSQL.setReadOnly(true);
        campaignGeneratorSQLBinder.addValueChangeListener(event -> {
            campaignSQL.setValue(campaignGeneratorSQLBinder.getBean().toSQL());
            System.out.println(campaignGeneratorSQLBinder.getBean().toSQL() + "otuside");
        });

        add(campaignSQL);
    }

    public class CampaignEditor extends CustomField<Campaign> {
        private final Binder<Campaign> campaignBinder = new Binder<>(Campaign.class);
        public CampaignEditor() {
            TextField campaignName = new TextField("Campaign Name");
            campaignBinder.bind(campaignName, Campaign::getName, Campaign::setName);
            TextField couponId = new TextField("Coupon Id");
            campaignBinder.bind(couponId, Campaign::getCouponId, Campaign::setCouponId);
            TextField layoutId = new TextField("Layout Id");
            campaignBinder.bind(layoutId, Campaign::getLayoutId, Campaign::setLayoutId);
            TextField percentage = new TextField("Percentage");
            campaignBinder.bind(percentage, Campaign::getPercentage, Campaign::setPercentage);
            TextField volume = new TextField("Volume");
            campaignBinder.bind(volume, Campaign::getVolume, Campaign::setVolume);
            TextField description = new TextField("Description");
            campaignBinder.bind(description, Campaign::getDescription, Campaign::setDescription);
            TextField discountTitle = new TextField("Discount Title");
            campaignBinder.bind(discountTitle, Campaign::getDiscountTitle, Campaign::setDiscountTitle);
            TextField recommendationCode = new TextField("Recommendation Code");
            campaignBinder.bind(recommendationCode, Campaign::getRecommendationCode, Campaign::setRecommendationCode);
            add(campaignName, couponId, layoutId, percentage, volume, description, discountTitle, recommendationCode);
            campaignBinder.addValueChangeListener(event -> updateValue());
        }

        @Override
        protected Campaign generateModelValue () {
            return campaignBinder.getBean();
        }

        @Override
        protected void setPresentationValue (Campaign campaign) {
            campaignBinder.setBean(campaign);
        }
    }
}
