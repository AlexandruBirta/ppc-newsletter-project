var path = require('path');
const CopyPlugin = require("copy-webpack-plugin");
module.exports = {
    entry: './src/main/js/app.js',
    devtool: 'sourcemaps',
    cache: false,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js',

    },
    plugins: [
        new CopyPlugin({
            patterns: [
                { from: "src/main/resources/img/", to: "src/main/resources/static/built/img/" },

            ],
        }),
    ],
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }],
            },
            {
                test: /\.css$/,
                use: [
                    "style-loader",
                    {
                        loader: "css-loader",
                        options: {
                            importLoaders: 1,
                            modules: true,
                        },
                    },
                ],
                include: /\.module\.css$/,
            },
            {
                test: /\.css$/,
                use: ["style-loader", "css-loader"],
                exclude: /\.module\.css$/,
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                loader: 'file-loader',
                options: {
                    publicPath: 'built'
                }
            },
            { test: /\.(png|jpg)$/, loader: 'url-loader?limit=8192' },
        ],

    }
};


