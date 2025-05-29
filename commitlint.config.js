module.exports = {
  parserPreset: {
    parserOpts: {
      // Prefijo tipo LPS-123 es opcional
      headerPattern: /^(?:([A-Z]+-\d+)\s)?(feat|fix|chore|docs|style|refactor|test|ci)(\([a-zA-Z0-9\-]+\))?:\s.+$/,
      headerCorrespondence: ["jira", "type", "scope", "subject"]
    }
  },
  rules: {
    "type-enum": [
      2,
      "always",
      ["feat", "fix", "chore", "docs", "style", "refactor", "test", "ci"]
    ],
    "header-max-length": [2, "always", 100]
  }
};