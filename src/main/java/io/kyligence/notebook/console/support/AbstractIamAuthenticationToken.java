/*
 *
 * Copyright (C) 2021 Kyligence Inc. All rights reserved.
 *
 * http://kyligence.io
 *
 * This software is the confidential and proprietary information of
 * Kyligence Inc. ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * Kyligence Inc.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package io.kyligence.notebook.console.support;

import io.kyligence.saas.base.AuthInfo;
import io.kyligence.saas.enums.iam.SessionTypeEnum;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public abstract class AbstractIamAuthenticationToken extends AbstractAuthenticationToken {

    private AuthInfo authInfo;

    public AbstractIamAuthenticationToken(AuthInfo authInfo) {
        super(Collections.emptyList());
        super.setAuthenticated(true);
        this.authInfo = authInfo;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public boolean type(SessionTypeEnum sessionTypeEnum) {
        return sessionTypeEnum.is(authInfo.getSessionType());
    }

}
