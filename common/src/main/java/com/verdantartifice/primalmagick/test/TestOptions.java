package com.verdantartifice.primalmagick.test;

public record TestOptions(int timeoutTicks, String batch, boolean skyAccess, int rotationSteps, boolean required, boolean manualOnly, String template, long setupTicks, int attempts, int requiredSuccesses) {
    public static Builder builder(String template) {
        return new Builder(template);
    }
    
    public static class Builder {
        private int timeoutTicks = 100;
        private String batch = TestUtils.DEFAULT_BATCH;
        private boolean skyAccess = false;
        private int rotationSteps = 0;
        private boolean required = true;
        private boolean manualOnly = false;
        private String template;
        private long setupTicks = 0L;
        private int attempts = 1;
        private int requiredSuccesses = 1;

        public Builder(String template) {
            this.template = template;
        }
        
        public Builder timeoutTicks(int ticks) {
            this.timeoutTicks = ticks;
            return this;
        }
        
        public Builder batch(String batch) {
            this.batch = batch;
            return this;
        }
        
        public Builder skyAccess() {
            this.skyAccess = true;
            return this;
        }
        
        public Builder rotationSteps(int steps) {
            this.rotationSteps = steps;
            return this;
        }
        
        public Builder optional() {
            this.required = false;
            return this;
        }
        
        public Builder manualOnly() {
            this.manualOnly = true;
            return this;
        }
        
        public Builder template(String template) {
            this.template = template;
            return this;
        }
        
        public Builder setupTicks(long ticks) {
            this.setupTicks = ticks;
            return this;
        }
        
        public Builder attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }
        
        public Builder requiredSuccesses(int successes) {
            this.requiredSuccesses = successes;
            return this;
        }
        
        public TestOptions build() {
            return new TestOptions(this.timeoutTicks, this.batch, this.skyAccess, this.rotationSteps, this.required, this.manualOnly, this.template, this.setupTicks, this.attempts, this.requiredSuccesses);
        }
    }
}
