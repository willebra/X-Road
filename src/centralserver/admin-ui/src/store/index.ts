/*
 * The MIT License
 * Copyright (c) 2019- Nordic Institute for Interoperability Solutions (NIIS)
 * Copyright (c) 2018 Estonian Information System Authority (RIA),
 * Nordic Institute for Interoperability Solutions (NIIS), Population Register Centre (VRK)
 * Copyright (c) 2015-2017 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
Initialises the Vuex store.
Adds store modules and plugins.
*/
import Vue from 'vue';
import Vuex, { StoreOptions } from 'vuex';
import { module as notifications } from './modules/notifications';
import { RootState } from '@/global';
import { module as user } from './modules/user';
import { module as tokens } from './modules/tokens';
import { module as initialization } from './modules/initialization';
import { module as system } from './modules/system';

import VuexPersistence from 'vuex-persist';

Vue.use(Vuex);

const vuexLocal = new VuexPersistence({
  storage: window.localStorage,
  modules: ['user', 'system'],
});

const store: StoreOptions<RootState> = {
  state: {
    version: '1.0.0', // a simple property
  },
  modules: {
    notifications,
    user,
    tokens,
    initialization,
    system,
  },
  plugins: [vuexLocal.plugin],
};

export default new Vuex.Store<RootState>(store);
