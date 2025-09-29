#!/usr/bin/env node

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

// 参考：https://qiita.com/ebisawa_a/items/5e6872b82da116892b2f
// Shai-Hulud感染パッケージリスト
const shaiHuludCompromisedPackages = [
    { package: "@ahmedhfarag/ngx-perfect-scrollbar", versions: ["20.0.20"] },
    { package: "@ahmedhfarag/ngx-virtual-scroller", versions: ["4.0.4"] },
    { package: "@art-ws/common", versions: ["2.0.22"] },
    { package: "@art-ws/config-eslint", versions: ["2.0.4", "2.0.5"] },
    { package: "@art-ws/config-ts", versions: ["2.0.7", "2.0.8"] },
    { package: "@art-ws/db-context", versions: ["2.0.21"] },
    { package: "@art-ws/di", versions: ["2.0.28"] },
    { package: "@art-ws/di-node", versions: ["2.0.13"] },
    { package: "@art-ws/eslint", versions: ["1.0.5", "1.0.6"] },
    { package: "@art-ws/fastify-http-server", versions: ["2.0.24"] },
    { package: "@art-ws/http-server", versions: ["2.0.21"] },
    { package: "@art-ws/openapi", versions: ["0.1.9"] },
    { package: "@art-ws/package-base", versions: ["1.0.5", "1.0.6"] },
    { package: "@art-ws/prettier", versions: ["1.0.5", "1.0.6"] },
    { package: "@art-ws/slf", versions: ["2.0.15"] },
    { package: "@art-ws/ssl-info", versions: ["1.0.9", "1.0.10"] },
    { package: "@art-ws/web-app", versions: ["1.0.3", "1.0.4"] },
    { package: "@crowdstrike/commitlint", versions: ["8.1.1", "8.1.2"] },
    { package: "@crowdstrike/falcon-shoelace", versions: ["0.4.1"] },
    { package: "@crowdstrike/foundry-js", versions: ["0.19.1", "0.19.2"] },
    { package: "@crowdstrike/glide-core", versions: ["0.34.2", "0.34.3"] },
    { package: "@crowdstrike/logscale-dashboard", versions: ["1.205.1", "1.205.2"] },
    { package: "@crowdstrike/logscale-file-editor", versions: ["1.205.1", "1.205.2"] },
    { package: "@crowdstrike/logscale-parser-edit", versions: ["1.205.1", "1.205.2"] },
    { package: "@crowdstrike/logscale-search", versions: ["1.205.1", "1.205.2"] },
    { package: "@crowdstrike/tailwind-toucan-base", versions: ["5.0.1", "5.0.2"] },
    { package: "@ctrl/deluge", versions: ["7.2.1", "7.2.2"] },
    { package: "@ctrl/golang-template", versions: ["1.4.2", "1.4.3"] },
    { package: "@ctrl/magnet-link", versions: ["4.0.3", "4.0.4"] },
    { package: "@ctrl/ngx-codemirror", versions: ["7.0.1", "7.0.2"] },
    { package: "@ctrl/ngx-csv", versions: ["6.0.1", "6.0.2"] },
    { package: "@ctrl/ngx-emoji-mart", versions: ["9.2.1", "9.2.2"] },
    { package: "@ctrl/ngx-rightclick", versions: ["4.0.1", "4.0.2"] },
    { package: "@ctrl/qbittorrent", versions: ["9.7.1", "9.7.2"] },
    { package: "@ctrl/react-adsense", versions: ["2.0.1", "2.0.2"] },
    { package: "@ctrl/shared-torrent", versions: ["6.3.1", "6.3.2"] },
    { package: "@ctrl/tinycolor", versions: ["4.1.1", "4.1.2"] },
    { package: "@ctrl/torrent-file", versions: ["4.1.1", "4.1.2"] },
    { package: "@ctrl/transmission", versions: ["7.3.1"] },
    { package: "@ctrl/ts-base32", versions: ["4.0.1", "4.0.2"] },
    { package: "@hestjs/core", versions: ["0.2.1"] },
    { package: "@hestjs/cqrs", versions: ["0.1.6"] },
    { package: "@hestjs/demo", versions: ["0.1.2"] },
    { package: "@hestjs/eslint-config", versions: ["0.1.2"] },
    { package: "@hestjs/logger", versions: ["0.1.6"] },
    { package: "@hestjs/scalar", versions: ["0.1.7"] },
    { package: "@hestjs/validation", versions: ["0.1.6"] },
    { package: "@nativescript-community/arraybuffers", versions: ["1.1.6", "1.1.7", "1.1.8"] },
    { package: "@nativescript-community/gesturehandler", versions: ["2.0.35"] },
    { package: "@nativescript-community/perms", versions: ["3.0.5", "3.0.6", "3.0.7", "3.0.8"] },
    { package: "@nativescript-community/sqlite", versions: ["3.5.2", "3.5.3", "3.5.4", "3.5.5"] },
    { package: "@nativescript-community/text", versions: ["1.6.9", "1.6.10", "1.6.11", "1.6.12"] },
    { package: "@nativescript-community/typeorm", versions: ["0.2.30", "0.2.31", "0.2.32", "0.2.33"] },
    { package: "@nativescript-community/ui-collectionview", versions: ["6.0.6"] },
    { package: "@nativescript-community/ui-document-picker", versions: ["1.1.27", "1.1.28"] },
    { package: "@nativescript-community/ui-drawer", versions: ["0.1.30"] },
    { package: "@nativescript-community/ui-image", versions: ["4.5.6"] },
    { package: "@nativescript-community/ui-label", versions: ["1.3.35", "1.3.36", "1.3.37"] },
    { package: "@nativescript-community/ui-material-bottom-navigation", versions: ["7.2.72", "7.2.73", "7.2.74", "7.2.75"] },
    { package: "@nativescript-community/ui-material-bottomsheet", versions: ["7.2.72"] },
    { package: "@nativescript-community/ui-material-core", versions: ["7.2.72", "7.2.73", "7.2.74", "7.2.75"] },
    { package: "@nativescript-community/ui-material-core-tabs", versions: ["7.2.72", "7.2.73", "7.2.74", "7.2.75"] },
    { package: "@nativescript-community/ui-material-ripple", versions: ["7.2.72", "7.2.73", "7.2.74", "7.2.75"] },
    { package: "@nativescript-community/ui-material-tabs", versions: ["7.2.72", "7.2.73", "7.2.74", "7.2.75"] },
    { package: "@nativescript-community/ui-pager", versions: ["14.1.36", "14.1.37", "14.1.38"] },
    { package: "@nativescript-community/ui-pulltorefresh", versions: ["2.5.4", "2.5.5", "2.5.6", "2.5.7"] },
    { package: "@nexe/config-manager", versions: ["0.1.1"] },
    { package: "@nexe/eslint-config", versions: ["0.1.1"] },
    { package: "@nexe/logger", versions: ["0.1.3"] },
    { package: "@nstudio/angular", versions: ["20.0.4", "20.0.5", "20.0.6"] },
    { package: "@nstudio/focus", versions: ["20.0.4", "20.0.5", "20.0.6"] },
    { package: "@nstudio/nativescript-checkbox", versions: ["2.0.6", "2.0.7", "2.0.8", "2.0.9"] },
    { package: "@nstudio/nativescript-loading-indicator", versions: ["5.0.1", "5.0.2", "5.0.3", "5.0.4"] },
    { package: "@nstudio/ui-collectionview", versions: ["5.1.11", "5.1.12", "5.1.13", "5.1.14"] },
    { package: "@nstudio/web", versions: ["20.0.4"] },
    { package: "@nstudio/web-angular", versions: ["20.0.4"] },
    { package: "@nstudio/xplat", versions: ["20.0.5", "20.0.6", "20.0.7"] },
    { package: "@nstudio/xplat-utils", versions: ["20.0.5", "20.0.6", "20.0.7"] },
    { package: "@operato/board", versions: ["9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42", "9.0.43", "9.0.44", "9.0.45", "9.0.46"] },
    { package: "@operato/data-grist", versions: ["9.0.29", "9.0.35", "9.0.36", "9.0.37"] },
    { package: "@operato/graphql", versions: ["9.0.22", "9.0.35", "9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42", "9.0.43", "9.0.44", "9.0.45", "9.0.46"] },
    { package: "@operato/headroom", versions: ["9.0.2", "9.0.35", "9.0.36", "9.0.37"] },
    { package: "@operato/help", versions: ["9.0.35", "9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42", "9.0.43", "9.0.44", "9.0.45", "9.0.46"] },
    { package: "@operato/i18n", versions: ["9.0.35", "9.0.36", "9.0.37"] },
    { package: "@operato/input", versions: ["9.0.27", "9.0.35", "9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42", "9.0.43", "9.0.44", "9.0.45", "9.0.46"] },
    { package: "@operato/layout", versions: ["9.0.35", "9.0.36", "9.0.37"] },
    { package: "@operato/popup", versions: ["9.0.22", "9.0.35", "9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42", "9.0.43", "9.0.44", "9.0.45", "9.0.46"] },
    { package: "@operato/pull-to-refresh", versions: ["9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42"] },
    { package: "@operato/shell", versions: ["9.0.22", "9.0.35", "9.0.36", "9.0.37", "9.0.38", "9.0.39"] },
    { package: "@operato/styles", versions: ["9.0.2", "9.0.35", "9.0.36", "9.0.37"] },
    { package: "@operato/utils", versions: ["9.0.22", "9.0.35", "9.0.36", "9.0.37", "9.0.38", "9.0.39", "9.0.40", "9.0.41", "9.0.42", "9.0.43", "9.0.44", "9.0.45", "9.0.46"] },
    { package: "@teselagen/bounce-loader", versions: ["0.3.16", "0.3.17"] },
    { package: "@teselagen/liquibase-tools", versions: ["0.4.1"] },
    { package: "@teselagen/range-utils", versions: ["0.3.14", "0.3.15"] },
    { package: "@teselagen/react-list", versions: ["0.8.19", "0.8.20"] },
    { package: "@teselagen/react-table", versions: ["6.10.19"] },
    { package: "@thangved/callback-window", versions: ["1.1.4"] },
    { package: "@things-factory/attachment-base", versions: ["9.0.43", "9.0.44", "9.0.45", "9.0.46", "9.0.47", "9.0.48", "9.0.49", "9.0.50"] },
    { package: "@things-factory/auth-base", versions: ["9.0.43", "9.0.44", "9.0.45"] },
];

// 色付きコンソール出力
const colors = {
    reset: '\x1b[0m',
    bright: '\x1b[1m',
    red: '\x1b[31m',
    green: '\x1b[32m',
    yellow: '\x1b[33m',
    blue: '\x1b[34m',
    magenta: '\x1b[35m',
    cyan: '\x1b[36m'
};

const log = {
    error: (msg) => console.log(`${colors.red}❌ ${msg}${colors.reset}`),
    success: (msg) => console.log(`${colors.green}✅ ${msg}${colors.reset}`),
    warning: (msg) => console.log(`${colors.yellow}⚠️  ${msg}${colors.reset}`),
    info: (msg) => console.log(`${colors.blue}ℹ️  ${msg}${colors.reset}`),
    danger: (msg) => console.log(`${colors.red}🚨 ${msg}${colors.reset}`),
    header: (msg) => console.log(`${colors.cyan}${colors.bright}=== ${msg} ===${colors.reset}`)
};

// パッケージ感染チェック関数
function isCompromised(packageName, version) {
    const found = shaiHuludCompromisedPackages.find(p => p.package === packageName);
    if (!found) return false;
    if (!version) return { compromised: true, versions: found.versions };

    // バージョンの正規化（^や~を除去）
    const cleanVersion = version.replace(/^[\^~]/, '');
    const isCompromisedVersion = found.versions.includes(cleanVersion);

    return {
        compromised: true,
        isCompromisedVersion,
        compromisedVersions: found.versions,
        currentVersion: cleanVersion
    };
}

// package-lock.jsonを解析
function analyzePackageLock(filePath) {
    try {
        const content = fs.readFileSync(filePath, 'utf8');
        const lockData = JSON.parse(content);
        const compromised = [];

        // node_modules形式とpackages形式の両方をチェック
        const packages = lockData.packages || {};
        const dependencies = lockData.dependencies || {};

        // packages形式をチェック（npm v7+）
        for (const [pkgPath, pkgData] of Object.entries(packages)) {
            if (pkgPath === "") continue; // ルートパッケージをスキップ

            const packageName = pkgPath.startsWith('node_modules/')
                ? pkgPath.substring('node_modules/'.length)
                : pkgPath;

            const result = isCompromised(packageName, pkgData.version);
            if (result.compromised) {
                compromised.push({
                    package: packageName,
                    version: pkgData.version,
                    result
                });
            }
        }

        // dependencies形式もチェック（npm v6以前）
        function checkDependencies(deps, prefix = '') {
            for (const [pkgName, pkgData] of Object.entries(deps)) {
                const fullName = prefix ? `${prefix}/${pkgName}` : pkgName;
                const result = isCompromised(fullName, pkgData.version);
                if (result.compromised) {
                    compromised.push({
                        package: fullName,
                        version: pkgData.version,
                        result
                    });
                }

                // 再帰的に依存関係をチェック
                if (pkgData.dependencies) {
                    checkDependencies(pkgData.dependencies, prefix);
                }
            }
        }

        checkDependencies(dependencies);

        return compromised;
    } catch (error) {
        throw new Error(`Failed to parse ${filePath}: ${error.message}`);
    }
}

// yarn.lockを解析
function analyzeYarnLock(filePath) {
    try {
        const content = fs.readFileSync(filePath, 'utf8');
        const compromised = [];

        // yarn.lockの構文解析
        const lines = content.split('\n');
        let currentEntry = null;
        let currentVersion = null;

        for (let i = 0; i < lines.length; i++) {
            const line = lines[i].trim();

            // パッケージ名の行（引用符で囲まれている）
            if (line.includes('@') && line.includes(':') && !line.startsWith(' ') && !line.startsWith('#')) {
                // 複数の範囲指定がある場合の処理 (例: "package@^1.0.0", "package@~1.0.1":)
                const packageMatch = line.match(/^"?([^"@]+(?:@[^"\/]+\/[^"@]+|@[^"\/]+)??)@[^"]*"?:/);
                if (packageMatch) {
                    currentEntry = packageMatch[1];
                    currentVersion = null;
                }
            }

            // バージョン行の検出
            if (line.startsWith('version ') && currentEntry) {
                const versionMatch = line.match(/version "([^"]+)"/);
                if (versionMatch) {
                    currentVersion = versionMatch[1];

                    // 感染チェック
                    const result = isCompromised(currentEntry, currentVersion);
                    if (result.compromised) {
                        compromised.push({
                            package: currentEntry,
                            version: currentVersion,
                            result
                        });
                    }
                }
                currentEntry = null; // 次のエントリのためにリセット
            }

            // 空行でエントリをリセット
            if (line === '') {
                currentEntry = null;
                currentVersion = null;
            }
        }

        return compromised;
    } catch (error) {
        throw new Error(`Failed to parse ${filePath}: ${error.message}`);
    }
}

// より高精度なyarn.lock解析（正規表現ベース）
function analyzeYarnLockAdvanced(filePath) {
    try {
        const content = fs.readFileSync(filePath, 'utf8');
        const compromised = [];

        // yarn.lockのエントリを正規表現で分割
        const entryRegex = /^"?([^"@\s]+(?:@[^"\/\s]+\/[^"@\s]+)??)@[^"]*"?:\s*\n(?:(?:\s+.*\n)*)\s*version\s+"([^"]+)"/gm;

        let match;
        while ((match = entryRegex.exec(content)) !== null) {
            const packageName = match[1];
            const version = match[2];

            const result = isCompromised(packageName, version);
            if (result.compromised) {
                compromised.push({
                    package: packageName,
                    version: version,
                    result
                });
            }
        }

        // フォールバック：行ベース解析
        if (compromised.length === 0) {
            return analyzeYarnLock(filePath);
        }

        return compromised;
    } catch (error) {
        throw new Error(`Failed to parse ${filePath}: ${error.message}`);
    }
}

// ディレクトリを調査
function scanDirectory(targetPath) {
    const absolutePath = path.resolve(targetPath);

    if (!fs.existsSync(absolutePath)) {
        log.error(`Path does not exist: ${absolutePath}`);
        return;
    }

    if (!fs.statSync(absolutePath).isDirectory()) {
        log.error(`Path is not a directory: ${absolutePath}`);
        return;
    }

    log.header(`Scanning directory: ${absolutePath}`);

    const subdirectories = fs.readdirSync(absolutePath)
        .filter(item => fs.statSync(path.join(absolutePath, item)).isDirectory());

    if (subdirectories.length === 0) {
        log.warning('No subdirectories found');
        return;
    }

    log.info(`Found ${subdirectories.length} subdirectories`);

    let totalCompromised = 0;
    let totalProjects = 0;
    const compromisedProjects = [];

    for (const subdir of subdirectories) {
        const projectPath = path.join(absolutePath, subdir);
        const packageLockPath = path.join(projectPath, 'package-lock.json');
        const yarnLockPath = path.join(projectPath, 'yarn.lock');
        const packageJsonPath = path.join(projectPath, 'package.json');

        console.log(`\n${colors.bright}📁 Checking: ${subdir}${colors.reset}`);

        // package.jsonの存在確認
        if (!fs.existsSync(packageJsonPath)) {
            log.info('No package.json found - skipping');
            continue;
        }

        totalProjects++;

        let hasLockFile = false;
        let compromised = [];

        // package-lock.json優先でチェック
        if (fs.existsSync(packageLockPath)) {
            hasLockFile = true;
            try {
                compromised = analyzePackageLock(packageLockPath);
                log.info('Analyzed package-lock.json');
            } catch (error) {
                log.error(`Error analyzing package-lock.json: ${error.message}`);
            }
        } else if (fs.existsSync(yarnLockPath)) {
            hasLockFile = true;
            try {
                compromised = analyzeYarnLockAdvanced(yarnLockPath);
                log.info('Analyzed yarn.lock');
            } catch (error) {
                log.error(`Error analyzing yarn.lock: ${error.message}`);
                // yarn.lock解析に失敗した場合はpackage.jsonをチェック
                hasLockFile = false;
            }
        }

        // package.jsonから直接チェック（ロックファイルがない場合）
        if (!hasLockFile) {
            try {
                const packageJson = JSON.parse(fs.readFileSync(packageJsonPath, 'utf8'));
                const allDeps = {
                    ...packageJson.dependencies,
                    ...packageJson.devDependencies,
                    ...packageJson.peerDependencies
                };

                for (const [pkgName, version] of Object.entries(allDeps)) {
                    const result = isCompromised(pkgName, version);
                    if (result.compromised) {
                        compromised.push({
                            package: pkgName,
                            version,
                            result,
                            source: 'package.json'
                        });
                    }
                }
                log.warning('No lock file - analyzed package.json only (versions may be ranges)');
            } catch (error) {
                log.error(`Error analyzing package.json: ${error.message}`);
            }
        }

        if (compromised.length > 0) {
            log.danger(`Found ${compromised.length} compromised packages!`);
            compromisedProjects.push({ project: subdir, packages: compromised });
            totalCompromised += compromised.length;

            for (const pkg of compromised) {
                const status = pkg.result.isCompromisedVersion ?
                    `${colors.red}INFECTED VERSION${colors.reset}` :
                    `${colors.yellow}Package compromised (different version)${colors.reset}`;

                console.log(`   ${colors.red}🦠 ${pkg.package}@${pkg.version}${colors.reset} - ${status}`);
                if (!pkg.result.isCompromisedVersion) {
                    console.log(`      Compromised versions: ${pkg.result.compromisedVersions.join(', ')}`);
                }
            }
        } else {
            log.success('No compromised packages found');
        }
    }

    // 最終サマリー
    console.log(`\n${colors.cyan}${colors.bright}=== FINAL SUMMARY ===${colors.reset}`);
    console.log(`Total projects scanned: ${totalProjects}`);
    console.log(`Projects with compromised packages: ${compromisedProjects.length}`);
    console.log(`Total compromised package instances: ${totalCompromised}`);

    if (compromisedProjects.length > 0) {
        console.log(`\n${colors.red}${colors.bright}🚨 PROJECTS REQUIRING IMMEDIATE ATTENTION:${colors.reset}`);
        for (const project of compromisedProjects) {
            console.log(`${colors.red}• ${project.project}${colors.reset} (${project.packages.length} packages)`);
        }

        console.log(`\n${colors.yellow}${colors.bright}RECOMMENDED ACTIONS:${colors.reset}`);
        console.log('1. Update all compromised packages to safe versions');
        console.log('2. Rotate all authentication credentials (GitHub, npm, cloud)');
        console.log('3. Check for unauthorized repositories or workflows');
        console.log('4. Scan for malicious files in node_modules');
        console.log('5. Review recent git commits for suspicious activity');
    } else {
        log.success('All scanned projects appear to be clean!');
    }
}

// メイン実行
function main() {
    const args = process.argv.slice(2);

    if (args.length === 0) {
        console.log(`${colors.cyan}Shai-Hulud npm Package Vulnerability Scanner${colors.reset}`);
        console.log('\nUsage:');
        console.log('  node shai-hulud-checker.js <target_directory>');
        console.log('\nExample:');
        console.log('  node shai-hulud-checker.js /path/to/projects');
        console.log('\nThis will scan all subdirectories for package-lock.json files');
        console.log('and check for compromised packages from the Shai-Hulud attack.');
        process.exit(1);
    }

    const targetPath = args[0];
    scanDirectory(targetPath);
}

main();
