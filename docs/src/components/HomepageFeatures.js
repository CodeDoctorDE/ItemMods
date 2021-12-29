import React from 'react';
import clsx from 'clsx';
import styles from './HomepageFeatures.module.css';

const FeatureList = [
    {
        title: 'Modular',
        Svg: require('../../static/img/squares-four.svg').default,
        description: (
            <>
                Everyone can build their own addons. See the <a href={"/api"}>api documentation</a> for more information.
            </>
        ),
    },
    {
        title: 'Open source and free',
        Svg: require('../../static/img/code.svg').default,
        description: (
            <>
                The source code is available on <a href={"https://github.com/CodeDoctorDE/ItemMods"}>GitHub</a>.
            </>
        ),
    },
    {
        title: 'Customizable',
        Svg: require('../../static/img/pencil.svg').default,
        description: (
            <>
                You can easily customize the look and functionality of items and blocks.
                Share your own packs with the community.
            </>
        ),
    },
];

function Feature({Svg, title, description}) {
    return (
        <div className={clsx('col col--4')}>
            <div className="text--center">
                <Svg className={styles.featureSvg} alt={title}/>
            </div>
            <div className="text--center padding-horiz--md">
                <h3>{title}</h3>
                <p>{description}</p>
            </div>
        </div>
    );
}

export default function HomepageFeatures() {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}
