import React from "react";
import Layout from "@theme/Layout";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import styles from "./list.module.css";
import {Mods} from "../data/resources";

export default function ModsPage() {
    const {siteConfig} = useDocusaurusContext();
    return (
        <Layout
            title={`Hello from ${siteConfig.title}`}
            description="Mods"
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
                    {Mods.map((mod) => (
                        <div className={"card " + styles.listItem} key={mod.title}>
                            <div className="card__header">
                                <h3>{mod.title}</h3>
                            </div>
                            <div className="card__body">
                                <p>{mod.description}</p>
                            </div>
                            <div className="card__footer">
                                <div className="button-group button-group--block">
                                    <a
                                        href={mod.link}
                                        target="__blank"
                                        className="button button--primary"
                                    >
                                        Open
                                    </a>
                                    {mod.source && (
                                        <a
                                            href={mod.source}
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
