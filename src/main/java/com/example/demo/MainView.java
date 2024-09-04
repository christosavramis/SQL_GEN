package com.example.demo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
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

        Button clearButton = new Button("Clear");
        clearButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearButton.addClickListener(event -> campaignGeneratorSQLBinder.setBean(new CampaignGeneratorSQL()));
        add(clearButton);

        CampaignEditor campaignEditor = new CampaignEditor();
        campaignGeneratorSQLBinder.bind(campaignEditor, CampaignGeneratorSQL::getCampaign, CampaignGeneratorSQL::setCampaign);
        add(campaignEditor);

        CouponEditor couponEditor = new CouponEditor(campaignEditor.getCampaignBinder());
        campaignGeneratorSQLBinder.bind(couponEditor, CampaignGeneratorSQL::getCoupon, CampaignGeneratorSQL::setCoupon);
        add(couponEditor);

        TextArea campaignSQL = new TextArea("Campaign SQL");
        campaignSQL.setReadOnly(true);
        campaignSQL.setValue(campaignGeneratorSQLBinder.getBean().toSQL());
        campaignGeneratorSQLBinder.addStatusChangeListener(event -> campaignSQL.setValue(campaignGeneratorSQLBinder.getBean().toSQL()));

        add(campaignSQL);
    }

    public class CouponEditor extends CustomField<Coupon> {
        private final Binder<Coupon> couponBinder = new Binder<>(Coupon.class);
        private final Binder<Campaign> campaignBinder;
        public CouponEditor(Binder<Campaign> campaignBinder) {
            this.campaignBinder = campaignBinder;
            couponBinder.setBean(new Coupon(new Campaign()));
            TextField couponCode = new TextField("Coupon Code");
            couponBinder.bind(couponCode, Coupon::getCouponCode, Coupon::setCouponCode);
            NumberField used = new NumberField("Used");
            couponBinder.bind(used, Coupon::getUsed, Coupon::setUsed);
            add(couponCode, used);
        }

        @Override
        protected Coupon generateModelValue () {
            Coupon coupon = new Coupon(campaignBinder.getBean());
            try {
                couponBinder.writeBean(coupon);
            } catch (ValidationException e) {
                System.out.println("Error while writing bean");
            }
            return coupon;
        }

        @Override
        protected void setPresentationValue (Coupon coupon) {
            couponBinder.readBean(coupon);
        }
    }

    public class CampaignEditor extends CustomField<Campaign> {
        @Getter
        private final Binder<Campaign> campaignBinder = new Binder<>(Campaign.class);
        public CampaignEditor() {
            campaignBinder.setBean(new Campaign());
            TextField campaignName = new TextField("Campaign Name");
            campaignBinder.bind(campaignName, Campaign::getName, Campaign::setName);
            TextField couponId = new TextField("Coupon Id");
            campaignBinder.bind(couponId, Campaign::getCouponId, Campaign::setCouponId);
            NumberField layoutId = new NumberField("Layout Id");
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
        }

        @Override
        protected Campaign generateModelValue () {
            Campaign campaign = new Campaign();
            try {
                campaignBinder.writeBean(campaign);
            } catch (ValidationException e) {
                System.out.println("Error while writing bean");
            }
            return campaign;
        }

        @Override
        protected void setPresentationValue (Campaign campaign) {
            campaignBinder.readBean(campaign);
        }
    }
}
