import { QwcHotReloadElement, html, css} from 'qwc-hot-reload-element';
import {enabled, strictRest, strictUtils, expectedEnv} from 'build-time-data';
import 'qwc/qwc-extension-link.js';
import {JsonRpc} from 'jsonrpc'
import 'qui-badge';
import '@vaadin/progress-bar';
import '@vaadin/grid';
import {columnBodyRenderer} from '@vaadin/grid/lit.js';
import '@vaadin/grid/vaadin-grid-sort-column.js';

export class QwcConfigurationProviderCard extends QwcHotReloadElement {
    jsonRpc = new JsonRpc(this);

    static styles = css`
        .identity {
            display: flex;
            justify-content: flex-start;
        }

        .strictDetails {
            padding-top: 10px;
            color: var(--lumo-contrast-70pct);
            font-size: smaller;
            text-align: center;
        }

        .description {
            padding-bottom: 10px;
        }

        .card-content {
            color: var(--lumo-contrast-90pct);
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
            padding: 10px 10px;
            height: 100%;
        }

        .card-content slot {
            display: flex;
            flex-flow: column wrap;
            padding-top: 5px;
        }
    `;

    static properties = {
        extensionName: {type: String},
        description: {type: String},
        guide: {type: String},
        namespace: {type: String},
        _providerURL: {type: String},
        _providedEntries: {type: Array},
    }

    constructor() {
        super();
    }

    connectedCallback() {
        super.connectedCallback();
        this.load();
    }

    load() {
        this.jsonRpc.getConfigurationProviderURL().then(response => this._providerURL = response.result);
        this.jsonRpc.getProvidedConfigurations({expectedValues: expectedEnv}).then(response => this._providedEntries = response.result);

    }

    hotReload(){
        this.load();
    }

    render() {
        if (!enabled) {
            return html`<h2 style="color: red">💀 This extension should not even be used, please stop wasting resources ! 💀</h2>`;
        }
        if (this._providerURL) {
            return html`
                <div class="card-content" slot="content">
                    <div class="identity">
                        <div class="description">${this.description}</div>
                    </div>
                    ${this._renderConfigDetails()}
                    ${this._renderStrictDetails()}
                    ${this._renderProvidedConf()}
                </div>`;
        } else {
            return html`
                <div style="color: var(--lumo-secondary-text-color);width: 95%;">
                    <div>Fetching information...</div>
                    <vaadin-progress-bar indeterminate></vaadin-progress-bar>
                </div>
            `;
        }
    }

    _renderCardLinks() {
        if (enabled) {
            return html`${pages.map(page => html`
                <qwc-extension-link slot="link"
                                    namespace="${this.namespace}"
                                    extensionName="${this.name}"
                                    iconName="${page.icon}"
                                    displayName="${page.title}"
                                    staticLabel="${page.staticLabel}"
                                    dynamicLabel="${page.dynamicLabel}"
                                    streamingLabel="${page.streamingLabel}"
                                    path="${page.id}"
                                    ?embed=${page.embed}
                                    externalUrl="${page.metadata.externalUrl}"
                                    dynamicUrlMethodName="${page.metadata.dynamicUrlMethodName}"
                                    webcomponent="${page.componentLink}">
                </qwc-extension-link>
            `)}`;
        } else {
            return html`<span>Disabled</span>`;
        }

    }

    _renderConfigDetails() {
        return html`
            <div>
                <span>Configuration provider URL: <a href="${this._providerURL}"><vaadin-icon icon="font-awesome-solid:link"></vaadin-icon>${this._providerURL}</a></span><br/>
            </div>`;
    }

    _renderStrictDetails() {
        let restStrictIcon = strictRest ? 'font-awesome-solid:bomb':'font-awesome-solid:truck-medical';
        let utilsStrictIcon = strictUtils ? 'font-awesome-solid:bomb':'font-awesome-solid:truck-medical';
        return html`
            <div>
                <span>Rest strict correction: <vaadin-icon icon="${restStrictIcon}"></vaadin-icon></span><br/>
                <span>Utils strict correction <vaadin-icon icon="${utilsStrictIcon}"></vaadin-icon></span><br/>
            </div>`;
    }

    _renderProvidedConf() {
        return html`<h3>Provided conferences</h3>
        <vaadin-grid .items="${this._providedEntries}" class="infogrid" all-rows-visible>
            <vaadin-grid-sort-column header='Key'
                                     path="key"
                                     ${columnBodyRenderer(this._keyRenderer, [])}>
            </vaadin-grid-sort-column>

            <vaadin-grid-sort-column
                    header="Value"
                    path="value"
                    ${columnBodyRenderer(this._valueRenderer, [])}>
            </vaadin-grid-sort-column>
        </vaadin-grid>`;
    }

    _keyRenderer(entry) {
        return html`<span>${entry.key}</span>`;
    }

    _valueRenderer(entry) {
        return html`<span>${entry.value}</span>`;
    }

}

customElements.define('qwc-configuration-provider-card', QwcConfigurationProviderCard);