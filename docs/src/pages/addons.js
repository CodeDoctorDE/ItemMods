import React from "react";
import Layout from "@theme/Layout";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import styles from "./list.module.css";
import {Addons} from "../data/resources";

export default function ModsPage() {
    const {siteConfig} = useDocusaurusContext();
    return (
        <Layout
            title={`Hello from ${siteConfig.title}`}
            description="Addons"
        >
            <div className={styles.searchBar}>
                <label htmlFor="search">Search</label>
                <input
                    type="name"
                    id="search"
                    name="search"
                    className={styles.searchInput}
                    onKeyUp={(event) => {
                        if (event.key === "Enter") window.find(event.target.value);
                    }}
                ></input>
            </div>
            <main>
                <div className={styles.list}>
                    {Addons.map((addon) => (
                        <div className={"card " + styles.listItem} key={addon.title}>
                            <div className="card__header">
                                <h3>{addon.title}</h3>
                            </div>
                            <div className="card__body">
                                <p>{addon.description}</p>
                            </div>
                            <div className="card__footer">
                                <div className="button-group button-group--block">
                                    <a
                                        href={addon.link}
                                        target="__blank"
                                        className="button button--primary"
                                    >
                                        Open
                                    </a>
                                    {addon.source && (
                                        <a
                                            href={addon.source}
                                            target="__blank"
                                            className="button button--secondary"
                                        >
                                            Source
                                        </a>
                                    )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </main>
        </Layout>
    );
}
